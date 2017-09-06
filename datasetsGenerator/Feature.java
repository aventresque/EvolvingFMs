
/* Representation of a feature :
 * id : line in the file, name : name in the file.
 * */

public class Feature {

	private int id;
	private String name;

	public Feature(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return "c " + id + " " + name + "\n";
	}

}
