package org.nzdis.robogame;

import org.nzdis.fragme.objects.FMeSerialised;
import org.nzdis.fragme.factory.FactoryObject;
import org.nzdis.fragme.factory.FragMeFactory;

/**
 * Serialised version of Message.
 */
public class Message_ser
    extends FMeSerialised {
  public Message_ser() {

  }

  private String performative = "";
  private String content = "";


  public String getPerformative(){
    return performative;
  }

  public String getContent(){
    return content;
  }

  public void setPerformative(String per){
    this.performative = per;
  }

  public void setContent(String con){
    this.content = con;
  }

//must be implemented
  static {
    FragMeFactory.addFactory(new Factory(), Message_ser.class);
  }


  private static class Factory
      extends FragMeFactory {
    protected FactoryObject create() {
      return new Message_ser();
    }
  }

  public Class getFMeObjectClassName() {
   return Message.class;
 }


}
