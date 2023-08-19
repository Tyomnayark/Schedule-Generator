import java.io.*;

class FilePrefixOutputStream extends OutputStream {
    private OutputStream outputStream;
    private boolean isConsole;
    private boolean atLineStart;
    private boolean addPrefix;

    public FilePrefixOutputStream(OutputStream outputStream, boolean isConsole) {
        this.outputStream = outputStream;
        this.isConsole = isConsole;
        this.atLineStart = true;
        this.addPrefix = false;
    }

    @Override
    public void write(int b) throws IOException {
        if (atLineStart && addPrefix && !isConsole) {
            outputStream.write((int) '#');
        }
        if (b == '\n') {
            atLineStart = true;
        } else {
            atLineStart = false;
        }
        outputStream.write(b);
    }

    public void enablePrefix() {
        addPrefix = true;
    }

    public void disablePrefix() {
        addPrefix = false;
    }
}
