package examples;

import io.grpc.ManagedChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.grpc.VertxChannelBuilder;

public class Client extends AbstractVerticle {
    public static void main(String[] args) {
        Vertx v = Vertx.vertx(new VertxOptions().setPreferNativeTransport(true));
        v.deployVerticle(new Client());
    }

    @Override
    public void start() {
        ManagedChannel channel = VertxChannelBuilder
                .forAddress(vertx, "localhost", 8080)
                .usePlaintext(true)
                .build();

    // Get a stub to use for interacting with the remote service
        GreeterGrpc.GreeterVertxStub stub = GreeterGrpc.newVertxStub(channel);
        HelloRequest request = HelloRequest.newBuilder().setName("Kacper").build();

    // Call the remote service
        stub.sayHello(request, ar -> {
            if (ar.succeeded()) {
                System.out.println("Got the server response: " + ar.result().getMessage());
            } else {
                System.out.println("Coult not reach server " + ar.cause().getMessage());
            }
        });
    }
}

