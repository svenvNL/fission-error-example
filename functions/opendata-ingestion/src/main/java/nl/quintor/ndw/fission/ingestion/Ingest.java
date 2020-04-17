package nl.quintor.ndw.fission.ingestion;

import io.fission.Context;
import io.fission.Function;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.GZIPInputStream;

public class Ingest implements Function {
    public ResponseEntity<?> call(RequestEntity requestEntity, Context context) {
        return ResponseEntity.ok("done");
    }
}
