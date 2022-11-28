package First.Spring;

import First.Spring.model.Messenger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;

@SpringBootApplication
public class SpringBasicsApplication {

	public static void main(String[] args) {
//		Messenger<String> messenger = new Messenger();
//		messenger.setMessage("23");
//		Messenger<HashMap> messenger2 = new Messenger();
//		HashMap<String, Object> objectObjectHashMap = new HashMap<>();
//		objectObjectHashMap.put("user","User 101");
//		objectObjectHashMap.put("email","User@gmail.com");
//		messenger2.setMessage(objectObjectHashMap);
//		Messenger<Character> s = new Messenger<>(9, 's');
//		System.out.println(messenger.sendMessage());
//		System.out.println(messenger2.sendMessage());
		SpringApplication.run(SpringBasicsApplication.class, args);
	}

}
