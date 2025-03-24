package lab.data;

import java.util.List;

import lab.Tools;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class Player  implements MyEntity{

	
	private Long id;
	private String firstName;
	private String lastName;
	private String nick;
	private List<Score> scores;

	public static Player generate() {
		return new Player(null, Tools.randomFistName(), Tools.randomLastName(), Tools.randomNick(), null);
	}

}
