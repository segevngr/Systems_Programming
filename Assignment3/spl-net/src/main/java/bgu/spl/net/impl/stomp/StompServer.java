package bgu.spl.net.impl.stomp;

import bgu.spl.net.api.*;
import bgu.spl.net.srv.ConnectionsImpl;
import bgu.spl.net.srv.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.Supplier;

public class StompServer {

    public static void main(String[] args) {

        final int port = Integer.parseInt(args[0]);

        if (args[1].equals("reactor"))
            Server.reactor(3, port, () -> new StompMessagingProtocolImpl(), MessageEncoderDecoderImpl::new).serve();

        else if (args[1].equals("tpc"))
            Server.threadPerClient(port, () -> new StompMessagingProtocolImpl(), MessageEncoderDecoderImpl::new).serve();

    }
}