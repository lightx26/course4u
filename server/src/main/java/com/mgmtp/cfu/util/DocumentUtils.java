package com.mgmtp.cfu.util;

import com.mgmtp.cfu.enums.DocumentType;
import com.mgmtp.cfu.exception.BadRequestRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static com.mgmtp.cfu.util.Constant.DOCUMENT_SIZE_LIMIT;

@Slf4j
public class DocumentUtils {

    public static String storageDocument(DocumentType type, MultipartFile file, String storageDir) {
        var fileName = type.name() + "_" + generateNewFileName(Objects.requireNonNull(file.getOriginalFilename()));
        storageFile(file, storageDir, fileName);
        return fileName;
    }

    //general file storaging method
    public static void storageFile(MultipartFile file, String storageDir, String fileName) {
        Path uploadPath = Paths.get(storageDir).toAbsolutePath().normalize();
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            File destinationFile = new File(uploadPath.toFile(), fileName);
            file.transferTo(destinationFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static String generateNewFileName(String originalFilename) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String uniqueID = UUID.randomUUID().toString().substring(0, 8);
        return timestamp + "_" + uniqueID + extension;
    }

    public static boolean isAllowedExtension(String fileName) {
        String[] allowedExtensions = { "pdf", "docx", "jpg", "jpeg", "png" };
        for (String ext : allowedExtensions) {
            if (fileName.toLowerCase().endsWith("." + ext)) {
                return true;
            }
        }
        return false;
    }

    public static void validate(MultipartFile[] certificates, MultipartFile[] payments, Long id) {
        if (certificates.length == 0 && payments.length == 0) {
            throw new BadRequestRuntimeException("Both certificates and payments are required. Please ensure all necessary documents are submitted.");
        }

        Stream.concat(Arrays.stream(certificates), Arrays.stream(payments)).forEach(multipartFile -> {
                    if (multipartFile.getSize() > DOCUMENT_SIZE_LIMIT)
                        throw new MaxUploadSizeExceededException(DOCUMENT_SIZE_LIMIT);
                    if (!isAllowedExtension(multipartFile.getOriginalFilename()))
                        throw new BadRequestRuntimeException("File extension can be uploaded: PDF, DOCX, JPG, JPEG, PNG");
                }

        );
        if (id == null)
            throw new BadRequestRuntimeException("Id must be not null");

    }
}
