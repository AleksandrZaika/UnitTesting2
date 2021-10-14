package core;

public class Station implements Comparable<Station>
{
    private core.Line line;
    private String name;

    public Station(String name, core.Line line)
    {
        this.name = name;
        this.line = line;
    }

    public core.Line getLine()
    {
        return line;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public int compareTo(Station station)
    {
        int lineComparison = line.compareTo(station.getLine());
        if(lineComparison != 0) {
            return lineComparison;
        }
        return name.compareToIgnoreCase(station.getName());
    }

    @Override
    public boolean equals(Object obj)
    {
        return compareTo((Station) obj) == 0;
    }

    @Override
    public String toString()
    {
        return name;
    }
}