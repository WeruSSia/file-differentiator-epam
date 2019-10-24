package file_differentiator;

import file_differentiator.file_extension.FileExtension;
import file_differentiator.file_extension.Gif;
import file_differentiator.file_extension.Jpg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class FileDifferentiator {

    private Set<FileExtension> handledExtensions;

    public FileDifferentiator() {
        handledExtensions = new HashSet<FileExtension>() {{
            add(new Jpg());
            add(new Gif());
        }};
    }

    public void differentiate(String filename) throws UnhandledExtensionException, IOException {
        byte[] fileContentAsByteArray = readFile(filename);
        String fileContentAsString = convertByteArrayToHexString(fileContentAsByteArray);
        String inputFileExtension = getFileExtension(filename);
        checkIfExtensionIsHandled(inputFileExtension);
        String realFileExtension = checkRealFileExtension(fileContentAsString);
    }

    private byte[] readFile(String filename) throws IOException {
        return Files.readAllBytes(Paths.get(filename));
    }

    private String convertByteArrayToHexString(byte[] data) {
        StringBuilder stringHex = new StringBuilder();
        for (byte b : data) {
            stringHex.append(String.format("%02X", b));
        }
        return stringHex.toString();
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private void checkIfExtensionIsHandled(String inputFileExtension) throws UnhandledExtensionException {
        for (FileExtension fileExtension : handledExtensions) {
            if (fileExtension.getName().equals(inputFileExtension)) {
                return;
            }
        }
        throw new UnhandledExtensionException();
    }

    private String checkRealFileExtension(String fileContent) {
        for (FileExtension fileExtension : handledExtensions) {
            if (fileExtension.doesFileContentMatchExtension(fileContent)) {
                return fileExtension.getName();
            }
        }
        return "txt";
    }
}
