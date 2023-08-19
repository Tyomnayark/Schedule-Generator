import java.io.IOException;
import java.io.OutputStream;

class MultiOutputStream extends OutputStream {
    private OutputStream[] outputStreams;

    public MultiOutputStream(OutputStream... outputStreams) {
        this.outputStreams = outputStreams;
    }

    @Override
    public void write(int b) throws IOException {
        for (OutputStream outputStream : outputStreams) {
            outputStream.write(b);
        }
    }

}
