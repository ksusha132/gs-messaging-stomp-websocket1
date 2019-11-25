package hello;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ExelFileProcessorImpl implements ExelFileProcessor {
    @Override
    public Status process(MultipartFile file) {
        // multuthreading mode
        // one single thread
        //
        return null;
    }
}
