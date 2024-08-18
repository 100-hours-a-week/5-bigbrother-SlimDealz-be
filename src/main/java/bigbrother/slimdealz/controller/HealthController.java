package bigbrother.slimdealz.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(HealthResponse.of());
    }

    public record HealthResponse(
            String message
    ) {

        public static HealthResponse of() {
            return new HealthResponse("ok");
        }
    }
}
