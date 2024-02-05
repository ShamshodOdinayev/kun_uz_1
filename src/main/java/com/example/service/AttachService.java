package com.example.service;

import com.example.dto.AttachDTO;
import com.example.entity.AttachEntity;
import com.example.exp.AppBadException;
import com.example.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.UUID;

@Service
public class AttachService {
    @Value("${server.url}")
    private String serverUrl;

    @Autowired
    private AttachRepository attachRepository;

    public String saveToSystem(MultipartFile file) { // mazgi.png
        try {
            File folder = new File("attaches");
            if (!folder.exists()) {
                folder.mkdir();
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get("attaches/" + file.getOriginalFilename()); // attaches/mazgi.png
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

 /*   public byte[] loadImage(String fileName) { // zari.jpg
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File("attaches/" + fileName));
            // attaches/zari.jpg
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);
            baos.flush();
            byte[] imageInByte;
            imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }*/

    public AttachDTO save(MultipartFile file) { // mazgi.png
        try {
            String pathFolder = getYmDString(); // 2022/04/23
            File folder = new File("uploads/" + pathFolder);
            if (!folder.exists()) { // uploads/2022/04/23
                folder.mkdirs();
            }
            String key = UUID.randomUUID().toString(); // dasdasd-dasdasda-asdasda-asdasd
            String extension = getExtension(file.getOriginalFilename()); // mp3/jpg/npg/mp4

            byte[] bytes = file.getBytes();
            Path path = Paths.get("uploads/" + pathFolder + "/" + key + "." + extension);
            //                         uploads/2022/04/23/dasdasd-dasdasda-asdasda-asdasd.jpg
            //                         uploads/ + Path + id + extension
            Files.write(path, bytes);

            AttachEntity entity = new AttachEntity();
            entity.setSize(file.getSize());
            entity.setExtension(extension);
            entity.setOriginalName(file.getOriginalFilename());
            entity.setCreatedData(LocalDateTime.now());
            entity.setId(key);
            entity.setPath(pathFolder);

            attachRepository.save(entity);

            return toDTO(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] open_general(String fileName) {
        byte[] data;
        try {
            Path file = Paths.get("attaches/" + fileName);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public String getYmDString() { // 2024/4/23
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day;
    }

    public byte[] loadImage(String attachId) { // dasdasd-dasdasda-asdasda-asdasd.jpg
        String id = attachId.substring(0, attachId.lastIndexOf("."));
        AttachEntity entity = get(id);
        byte[] data;
        try {
            Path file = Paths.get("uploads/" + entity.getPath() + "/" + attachId);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public AttachDTO toDTO(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setUrl(serverUrl + "/attach/open/" + entity.getId() + "." + entity.getExtension());
        return dto;
    }

    AttachEntity get(String id) {
        return attachRepository.findById(id).orElseThrow(() -> new AppBadException("File not found"));
    }

    public ResponseEntity<Resource> downloadFile(String attachId) {
        try {
            String id = attachId.substring(0, attachId.lastIndexOf("."));
            AttachEntity entity = get(id);
            Path file = Paths.get("uploads/" + entity.getPath() + "/" + attachId);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + entity.getOriginalName() + "\"").body(resource);
            } else {
                throw new AppBadException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new AppBadException("File not found");
        }
    }


}
