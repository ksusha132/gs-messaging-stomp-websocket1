package hello;

import org.springframework.web.multipart.MultipartFile;

public interface ExelFileProcessor {
    public Status process(MultipartFile file);
}
