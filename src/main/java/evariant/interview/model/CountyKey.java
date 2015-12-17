package evariant.interview.model;

/**
 * Created by ccayirog on 12/16/2015.
 */
public class CountyKey {
    private String name;
    private String state;


    public CountyKey(String name, String state) {
        this.name = name;
        this.state = state;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CountyKey county = (CountyKey) o;

        if (name != null ? !name.equals(county.name) : county.name != null) return false;
        if (state != null ? !state.equals(county.state) : county.state != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return "CountyKey{" +
                "name='" + name + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
