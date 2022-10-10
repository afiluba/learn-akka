package a02.simpletell;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ChildActor extends AbstractBehavior<ChildActor.Request> {
    static class Request {
        private String message;
        private ActorRef<String> replyTo;

        Request(String message, ActorRef<String> replyTo) {
            this.message = message;
            this.replyTo = replyTo;
        }

        public ActorRef<String> getReplyTo() {
            return replyTo;
        }

        public String getMessage() {
            return message;
        }
    }
    public static Behavior<ChildActor.Request> create() {
        return Behaviors.setup(ChildActor::new);
    }
    private ChildActor(ActorContext<ChildActor.Request> context) {
        super(context);
    }

    @Override
    public Receive<ChildActor.Request> createReceive() {
        return newReceiveBuilder()
                .onMessage(Request.class, this::respond)
                .build();
    }

    private Behavior<ChildActor.Request> respond(ChildActor.Request in) {
        getContext().getLog().debug("Got message " + in);
        getContext().getLog().debug("Adres " + getContext().getSelf());

        in.getReplyTo().tell("Got it");

        return this;
    }
}
