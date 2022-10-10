package a01.firstactor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;

/**
 * - an actor encapsulates its state and part of the application logic
 * - actors interact only through asynchronous messages and never through direct method calls
 * - each actor has a unique address and a mailbox in which other actors can deliver messages
 * - the actor will process all the messages in the mailbox in sequential order (the default implementation of the mailbox being a FIFO queue)
 * - the actor system is organized in a tree-like hierarchy
 * - an actor can create other actors, can send messages to any other actor and stop itself or any actor is has created
 */
public class FirstActor {
    public static void main(String[] args) {
        ActorSystem<String> testSystem = ActorSystem.create(Actor1.create(), "testSystem");

        testSystem.tell("test1");
        testSystem.tell("test2");
        testSystem.tell("test3");

        testSystem.terminate();
    }

    static class Actor1 extends AbstractBehavior<String> {
        /** Metoda fabrykująca upraszczająca tworzenie aktora */
        public static Behavior<String> create() {
            return Behaviors.setup(Actor1::new);
        }

        private Actor1(ActorContext<String> context) {
            super(context);
        }

        /** Metoda ustala dispatching komunikatów które obsługuje aktor */
        @Override
        public Receive<String> createReceive() {
            return newReceiveBuilder()
                    .onMessage(String.class, this::action1)
                    .build();
        }

        /** Reakcja na komunikat */
        private Behavior<String> action1(String in) {
            getContext().getLog().debug("Komunikat " + in);
            getContext().getLog().debug("Adres " + getContext().getSelf());
            return this;
        }
    }


}
