package com.mobile_app_server.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@RequestMapping("/api/pdfs")
@CrossOrigin(origins = "http://localhost:3000")
public class PdfController {

    @GetMapping("/view")
    public ResponseEntity<InputStreamResource> viewPdf(
            @RequestParam("path") String path,
            HttpServletRequest request
    ) throws IOException {

        File file = new File(path);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        long fileLength = file.length();
        String rangeHeader = request.getHeader("Range");

        long start = 0;
        long end = fileLength - 1;

        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] ranges = rangeHeader.substring(6).split("-");
            try {
                start = Long.parseLong(ranges[0]);
                if (ranges.length > 1 && !ranges[1].isEmpty()) {
                    end = Long.parseLong(ranges[1]);
                }
            } catch (NumberFormatException ignored) {}
        }

        if (end >= fileLength) end = fileLength - 1;
        long contentLength = end - start + 1;

        FileInputStream fis = new FileInputStream(file);
        fis.skip(start);
        InputStreamResource resource = new InputStreamResource(new LimitedInputStream(fis, contentLength));

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
        headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
        headers.set(HttpHeaders.CONTENT_RANGE, String.format("bytes %d-%d/%d", start, end, fileLength));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"");

        HttpStatus status = (rangeHeader == null) ? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT;
        return new ResponseEntity<>(resource, headers, status);
    }

    static class LimitedInputStream extends FilterInputStream {
        private long remaining;

        protected LimitedInputStream(InputStream in, long remaining) {
            super(in);
            this.remaining = remaining;
        }

        @Override
        public int read() throws IOException {
            if (remaining <= 0) return -1;
            int result = super.read();
            if (result != -1) remaining--;
            return result;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (remaining <= 0) return -1;
            len = (int) Math.min(len, remaining);
            int result = super.read(b, off, len);
            if (result != -1) remaining -= result;
            return result;
        }
    }
}

//@Slf4j
//@RestController
//@RequestMapping("/api/pdfs")
//public class PdfHandleController {
//
/// /    @GetMapping("/view")
/// /    public ResponseEntity<ResourceRegion> getPdf(
/// /            @RequestParam("path") String path,
/// /            @RequestHeader(value = "Range", required = false) String rangeHeader
/// /    ) throws IOException {
/// /
/// /        path = path.replace("\\", "/");
/// /
/// /        File file = new File(path);
/// /        if (!file.exists()) {
/// /            return ResponseEntity.notFound().build();
/// /        }
/// /
/// /        UrlResource resource = new UrlResource(file.toURI());
/// /        long contentLength = resource.contentLength();
/// /        System.out.println(contentLength);
/// /        final long CHUNK_SIZE = 1_000_000;
/// /
/// /        long start = 0;
/// /        long end = 0;
/// /        ResourceRegion region;
/// /
/// /        if (rangeHeader != null) {
/// /            String[] ranges = rangeHeader.replace("bytes=", "").split("-");
/// /            start = Long.parseLong(ranges[0]);
/// /            end = ranges.length > 1 && !ranges[1].isEmpty()
/// /                    ? Long.parseLong(ranges[1])
/// /                    : Math.min(start + CHUNK_SIZE - 1, contentLength - 1);
/// /
/// /            long rangeLength = Math.min(CHUNK_SIZE, end - start + 1);
/// /            region = new ResourceRegion(resource, start, rangeLength);
/// /        } else {
/// /            start = 0;
/// /            end = Math.min(CHUNK_SIZE - 1, contentLength - 1);
/// /            region = new ResourceRegion(resource, start, end - start + 1);
/// /        }
/// /
/// /        String contentRange = String.format("bytes %d-%d/%d", start, end, contentLength);
/// /
/// /        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
/// /                .contentType(MediaType.APPLICATION_PDF)
/// /                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
/// /                .body(region);
/// /    }
//
//    private static final long CHUNK_SIZE = 1024 * 1024; // 1MB
//    @GetMapping("/view")
//    public ResponseEntity<InputStreamResource> viewPdf(@RequestParam("path") String path,
//                                                       @RequestHeader(value = "Range", required = false) String rangeHeader)
//            throws IOException {
//
//        File file = new File(path);
//        if (!file.exists()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//
//        long fileLength = file.length();
//        long rangeStart = 0;
//        long rangeEnd = fileLength - 1;
//
//        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
//            String[] ranges = rangeHeader.substring(6).split("-");
//            try {
//                rangeStart = Long.parseLong(ranges[0]);
//                if (ranges.length > 1 && !ranges[1].isEmpty()) {
//                    rangeEnd = Long.parseLong(ranges[1]);
//                } else {
//                    rangeEnd = Math.min(rangeStart + CHUNK_SIZE - 1, fileLength - 1);
//                }
//            } catch (NumberFormatException e) {
//                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
//            }
//        } else {
//            rangeEnd = Math.min(CHUNK_SIZE - 1, fileLength - 1);
//        }
//
//        long contentLength = rangeEnd - rangeStart + 1;
//
//        InputStream inputStream = new FileInputStream(file);
//        inputStream.skip(rangeStart);
//
//        InputStream limitedStream = new InputStream() {
//            private long remaining = contentLength;
//            @Override
//            public int read() throws IOException {
//                if (remaining <= 0) return -1;
//                int data = inputStream.read();
//                if (data != -1) remaining--;
//                return data;
//            }
//            @Override
//            public int read(byte[] b, int off, int len) throws IOException {
//                if (remaining <= 0) return -1;
//                len = (int) Math.min(len, remaining);
//                int bytesRead = inputStream.read(b, off, len);
//                if (bytesRead > 0) remaining -= bytesRead;
//                return bytesRead;
//            }
//        };
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
//        headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
//        headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
//        headers.set(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
//        headers.setAccessControlAllowOrigin("http://localhost:3000");
//        headers.setAccessControlAllowCredentials(true);
//
//        HttpStatus status = (rangeHeader == null) ? HttpStatus.PARTIAL_CONTENT : HttpStatus.PARTIAL_CONTENT;
//
//        return ResponseEntity.status(status)
//                .headers(headers)
//                .body(new InputStreamResource(limitedStream));
//    }
////    @GetMapping("/view")
////    public ResponseEntity<byte[]> viewPdf(@RequestParam("path") String path,
////                                          @RequestHeader(value = "Range", required = false) String rangeHeader) throws IOException {
////
////        File file = new File(path);
////        if (!file.exists()) {
////            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
////        }
////
////        long fileLength = file.length();
////
////        // Nếu không có Range header, trả về toàn bộ file
////        if (rangeHeader == null || !rangeHeader.startsWith("bytes=")) {
////            byte[] data = Files.readAllBytes(file.toPath());
////            HttpHeaders headers = new HttpHeaders();
////            headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
////            headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength));
////            return ResponseEntity.ok().headers(headers).body(data);
////        }
////
////        // Parse Range header
////        String range = rangeHeader.substring(6);
////        String[] ranges = range.split("-");
////
////        long rangeStart = 0;
////        long rangeEnd = fileLength - 1;
////
////        try {
////            rangeStart = Long.parseLong(ranges[0]);
////            if (ranges.length > 1 && !ranges[1].isEmpty()) {
////                rangeEnd = Long.parseLong(ranges[1]);
////            } else {
////                // Nếu chỉ có start, tính end dựa trên chunk size
////                rangeEnd = Math.min(rangeStart + CHUNK_SIZE - 1, fileLength - 1);
////            }
////        } catch (NumberFormatException e) {
////            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).build();
////        }
////
////        // Validate range
////        if (rangeStart >= fileLength || rangeEnd >= fileLength || rangeStart > rangeEnd) {
////            HttpHeaders headers = new HttpHeaders();
////            headers.set(HttpHeaders.CONTENT_RANGE, "bytes */" + fileLength);
////            return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
////                    .headers(headers).build();
////        }
////
////        long contentLength = rangeEnd - rangeStart + 1;
////
////        // Đọc file
////        byte[] data = new byte[(int) contentLength];
////        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
////            raf.seek(rangeStart);
////            raf.readFully(data);
////        }
////
////        // Set headers
////        HttpHeaders headers = new HttpHeaders();
////        headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
////        headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
////        headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
////        headers.set(HttpHeaders.CONTENT_RANGE,
////                "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
////        headers.set(HttpHeaders.CACHE_CONTROL, "no-cache");
////        headers.set("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
////
////        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
////                .headers(headers)
////                .body(data);
////    }
//
//
////    @GetMapping("/view/contract")
////    public ResponseEntity<StreamingResponseBody> viewContractPdf(
////                                                                 HttpServletRequest request,
////                                                                 HttpServletResponse response) throws IOException {
////        final HttpHeaders responseHeaders = new HttpHeaders();
////        //buffer = 16KB
////        byte[] buffer = new byte[1024*1024];
////        responseHeaders.add("Content-Type", "application/pdf");
////        responseHeaders.add("Accept-Ranges", "bytes");
////        String range = request.getHeader("range");
////
////
////
////        String filePath = "C:\\Users\\ruy_pa_\\Downloads\\b.pdf";
////
////        if(range == null){
////
////
////            long fileSize = new File(filePath).length();
////            long rangeStart = 0;
////            long rangeEnd = 1048576;
////
////
////
////            StreamingResponseBody responseStream;
////            String contentLength = String.valueOf(rangeEnd - rangeStart + 1);
////            responseHeaders.add("Content-Length", contentLength);
////            responseHeaders.add("Content-Range", "bytes" + " " + rangeStart + "-" + rangeEnd + "/" + fileSize);
////
////
////            final long _rangeEnd = rangeEnd;
////            responseStream = os -> {
////                FileInputStream file = new FileInputStream(filePath);
////                try {
////                    long pos = rangeStart;
////                    file.skip(pos);
////                    while (pos < _rangeEnd) {
////                        file.read(buffer);
////                        os.write(buffer);
////                        pos += buffer.length;
////                    }
////                    os.flush();
////                } catch (Exception e) {
////
////                } finally {
////                    file.close();
////                }
////            };
////
////            return new ResponseEntity<>(responseStream, responseHeaders, HttpStatus.PARTIAL_CONTENT);
////
////
////        }
////        String[] ranges = range.split("-");
////        long fileSize = new File(filePath).length();
////        long rangeStart = Long.parseLong(ranges[0].substring(6));
////        long rangeEnd;
////        if (ranges.length > 1) {
////            rangeEnd = Long.parseLong(ranges[1]);
////        } else {
////            rangeEnd = fileSize - 1;
////        }
////
////        if (fileSize < rangeEnd) {
////            rangeEnd = fileSize - 1;
////        }
////
////
////        StreamingResponseBody responseStream;
////        String contentLength = String.valueOf(rangeEnd - rangeStart + 1);
////        responseHeaders.add("Content-Length", contentLength);
////        responseHeaders.add("Content-Range", "bytes" + " " + rangeStart + "-" + rangeEnd + "/" + fileSize);
////
////
////        final long _rangeEnd = rangeEnd;
////        responseStream = os -> {
////            FileInputStream file = new FileInputStream(filePath);
////            try {
////                long pos = rangeStart;
////                file.skip(pos);
////                while (pos < _rangeEnd) {
////                    file.read(buffer);
////                    os.write(buffer);
////                    pos += buffer.length;
////                }
////                os.flush();
////            } catch (Exception e) {
////
////            } finally {
////                file.close();
////            }
////        };
////
////        return new ResponseEntity<>(responseStream, responseHeaders, HttpStatus.PARTIAL_CONTENT);
////    }




