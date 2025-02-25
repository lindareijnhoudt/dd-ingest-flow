/*
 * Copyright (C) 2022 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.ingest.core.service;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionMethod;
import org.apache.tika.Tika;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class ZipFileHandler {

    private final Tika tika = new Tika();
    private final Path tempDir;

    private final Set<String> needToBeZipWrapped = Set.of(
        "application/zip",
        "application/zipped-shapefile",
        "application/fits-gzipped"
    );

    public ZipFileHandler(Path tempDir) {
        this.tempDir = tempDir;
    }

    public Optional<Path> wrapIfZipFile(Path path) throws IOException {
        if (!needsToBeWrapped(path)) {
            return Optional.empty();
        }

        var filename = Optional.ofNullable(path.getFileName())
            .map(Path::toString)
            .orElse("");

        var randomName = String.format("zip-wrapped-%s-%s.zip",
            filename, UUID.randomUUID());

        var tempFile = tempDir.resolve(randomName);

        var params = new ZipParameters();
        params.setCompressionMethod(CompressionMethod.STORE);

        try (var zip = new ZipFile(tempFile.toFile())) {
            zip.addFile(path.toFile());
        }

        return Optional.of(tempFile);
    }

    boolean needsToBeWrapped(Path path) throws IOException {
        var endsWithZip = Optional.ofNullable(path.getFileName())
            .map(Path::toString)
            .map(x -> x.endsWith(".zip"))
            .orElse(false);

        var isDetected = needToBeZipWrapped.contains(getMimeType(path));

        log.trace("Checking if path {} needs to be wrapped: endsWithZip={}, isDetected={}",
            path, endsWithZip, isDetected);

        return endsWithZip || isDetected;
    }

    String getMimeType(Path path) throws IOException {
        return tika.detect(path);
    }
}
