package First.Spring.model;

import lombok.Data;

@Data
public class Messenger <Anything> {
    private Anything message;

    private int no;

    public Messenger(){

    }
    public Messenger(int no, Anything message){
        this.no = no;
        this.message = message;
    }


    public Anything sendMessage(){
        return message;
    }
}
