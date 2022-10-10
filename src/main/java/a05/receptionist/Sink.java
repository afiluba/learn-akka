package a05.receptionist;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.ServiceKey;

public class Sink extends AbstractBehavior<String> {
    public static final ServiceKey<String> SERVICE_KEY =
            ServiceKey.create(String.class, "sink");

    public static Behavior<String> create() {
        return Behaviors.setup(
                Sink::new);
    }

    private Sink(ActorContext<String> context) {
        super(context);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .build();
    }
}
