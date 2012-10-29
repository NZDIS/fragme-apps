package org.nzdis.robogame;

import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FragMeFactory;
import org.nzdis.fragme.factory.FactoryObject;

/**
 * Message is a class used to notify other peers about game events such as
 * winning or restarting the game. The messages are "dispatched" in RoboGame.java.
 * The all consist of a performative and content.
 */
public class Message extends GameObject {

  /* performative defines the type of the message */
  private String performative = "";

  /* content of the message */
  private String content = "";

  public Message() {
  }

  public String getPerformative() {
    return performative;
  }

  public String getContent() {
    return content;
  }

  public void setPerformative(String per) {
    this.performative = per;
  }

  public void setContent(String con) {
    this.content = con;
  }


  /**
   * deserialize -- has to be implemented due to FMeObject
   */
  public void deserialize(FMeSerialised serObject) {
    this.performative = ( (Message_ser) serObject).getPerformative();
    this.content = ( (Message_ser) serObject).getContent();
  }

  /**
   * getSerializedObjectClassName -- has to be implemented due to FMeObject.
   */
  public Class getSerializedObjectClassName() {
    return Message_ser.class;
  }

  /**
   * serialize -- has to be implemented due to FMeObject.
   */
  public FMeSerialised serialize(FMeSerialised ser) {
    try {
      ( (Message_ser) ser).setPerformative(this.performative);
      ( (Message_ser) ser).setContent(this.content);

      return ser;
    }
    catch (Exception ex) {
      return null;
    }
  }

  /**
   * changedObject --   has to be implemented due to FMeObject.
   */
  public void changedObject() {
    game.dispatchMessage(performative, content);
  }

  public void deletedObject() {

  }


  // the factory
  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new Message();
    }
  }

  static {
    FragMeFactory.addFactory(new Factory(), Message.class);
  }

}
