package a03.ask;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.time.Duration;

public class ParentActor extends AbstractBehavior<ParentActor.Message> {
    interface Message {}

    static final class SendRequest implements Message {
    }
    private static final class AdaptedResponse implements Message {
        public final String message;

        public AdaptedResponse(String message) {
            this.message = message;
        }
    }

    public static Behavior<Message> create() {
        return Behaviors.setup(ParentActor::new);
    }

    private ParentActor(ActorContext<Message> context) {
        super(context);
    }

    @Override
    public Receive<ParentActor.Message> createReceive() {
        return newReceiveBuilder()
                .onMessage(ParentActor.SendRequest.class, this::sendRequest)
                .onMessage(ParentActor.AdaptedResponse.class, this::gotResponse)
                .build();
    }

    private Behavior<Message> sendRequest(ParentActor.SendRequest msg) {
        getContext().getLog().debug("Sending request");
        getContext().getLog().debug("Adres " + getContext().getSelf());

        ActorRef<ChildActor.Request> child = getContext().spawn(ChildActor.create(), "child" );

        getContext().ask(
                String.class,
                child,
                Duration.ofSeconds(3),
                (parent) -> new ChildActor.Request("Test message", parent),
                (rsp, throwable) -> {
                    if (rsp != null) {
                        return new AdaptedResponse(rsp);
                    } else {
                        return new AdaptedResponse("Exception");
                    }
                }
        );

        return this;
    }

    private Behavior<Message> gotResponse(ParentActor.AdaptedResponse s) {
        getContext().getLog().debug("Got response " + s);
        getContext().getLog().debug("Adres " + getContext().getSelf());

        return this;
    }
}
