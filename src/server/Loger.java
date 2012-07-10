package server;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Verilen tum output streamlere loglama yapan yardimci sinif
 * @author mustafa
 *
 */
public class Loger {
    private LinkedList<PrintStream> outputStreams;

    public Loger() {
        outputStreams = new LinkedList<PrintStream>();
    }

    public void addPrintStream(PrintStream out) {
        outputStreams.add(out);
    }

    public void log(String msg) {
        ListIterator<PrintStream> iter = outputStreams.listIterator();
        while (iter.hasNext()) {
            PrintStream out = iter.next();
            out.println(msg);
            out.flush();
        }
    }
    
    public void closeAll() {
    	ListIterator<PrintStream> iter = outputStreams.listIterator();
        while (iter.hasNext()) {
        	PrintStream out = iter.next();
        	out.close();
        }
	}
}
