package a02.simpletell;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ParentActor extends AbstractBehavior<String> {
    public static Behavior<String> create() {
        return Behaviors.setup(ParentActor::new);
    }

    private ParentActor(ActorContext<String> context) {
        super(context);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder()
                .onMessageEquals("start", this::sendRequest)
                .onAnyMessage(this::gotResponse)
                .build();
    }

    private Behavior<String> sendRequest() {
        getContext().getLog().debug("Sending request");
        getContext().getLog().debug("Adres " + getContext().getSelf());

        ActorRef<ChildActor.Request> child = getContext().spawn(ChildActor.create(), "child" );
        child.tell(new ChildActor.Request("Test message", getContext().getSelf()));

        return this;
    }

    private Behavior<String> gotResponse(String s) {
        getContext().getLog().debug("Got response " + s);
        getContext().getLog().debug("Adres " + getContext().getSelf());

        return this;
    }
}
