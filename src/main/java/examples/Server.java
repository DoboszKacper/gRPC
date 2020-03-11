package examples;

import io.grpc.BindableService;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.vertx.core.*;
import io.vertx.grpc.BlockingServerInterceptor;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

public class Server extends AbstractVerticle {
    public static void main(String[] args) {
        Vertx v = Vertx.vertx(new VertxOptions().setPreferNativeTransport(true));
        v.deployVerticle(new Server());

    }

    @Override
    public void start() throws Exception {

        MyInterceptor myInterceptor = new MyInterceptor();
        ServerInterceptor wrapped = BlockingServerInterceptor.wrap(vertx, myInterceptor);

        // Create the server
        VertxServer rpcServer = VertxServerBuilder
                .forAddress(vertx, "localhost", 8080)
                .addService(ServerInterceptors.intercept(new GreeterGrpc.GreeterVertxImplBase() {
                    @Override
                    public void sayHello(HelloRequest request, Future<HelloReply> response) {
                        System.out.println("Hello " + request.getName());
                        response.complete(HelloReply.newBuilder().setMessage(request.getName()).build());
                    }
                }, wrapped))
                .build();

        // Start it
        rpcServer.start();
    }

}
