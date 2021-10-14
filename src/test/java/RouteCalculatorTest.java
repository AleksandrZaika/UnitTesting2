import core.Line;
import core.Station;
import junit.framework.TestCase;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RouteCalculatorTest extends TestCase {

    StationIndex stationIndex;
    RouteCalculator routeCalculator;

    Line one;
    Line two;
    Line three;

    //1 линия
    Station prohodnaja;
    Station podjemnaja;
    Station kabinetnaja;
    Station ofisnaja;

    //2 линия
    Station dorozhnaja;
    Station remontnaja;
    Station znakovaja;
    Station bordjurnaja;

    //3 линия
    Station magazinnaja;
    Station rynochnaja;
    Station bazarnaja;
    Station vokzalnaja;

    List<Station> routeNoTransfer;
    List<Station> routeOneTransfer;
    List<Station> routeTwoTransfer;
//      схема нашего метро
//        1  2              prohodnaja      dorozhnaja
//        1  2--3           podjemnaja      remontnaja      magazinnaja
//        1--2  3           kabinetnaja --- znakovaja       rynochnaja
//        1  2--3           ofisnaja        bordjurnaja --- bazarnaja
//              3                                           vokzalnaja
//

    @Override
    protected void setUp() throws Exception {

        stationIndex = new StationIndex();
        routeCalculator = new RouteCalculator(stationIndex);

        //обзываем линии и добавляем в индекс
        one = new Line(1, "первая");
        two = new Line(2, "вторая");
        three = new Line(3, "третья");

        //обзываем станции 1 линии и добавляем в линию
        prohodnaja = new Station("Проходная", one);
        podjemnaja = new Station("Подъёмная", one);
        kabinetnaja = new Station("Кабинетная", one);
        ofisnaja = new Station("Офисная", one);

        //обзываем станции 2 линии и добавляем в линию
        dorozhnaja = new Station("Дорожная", two);
        remontnaja = new Station("Ремонтная", two);
        znakovaja = new Station("Знаковая", two);
        bordjurnaja = new Station("Бордюрная", two);

        //обзываем станции 3 линии и добавляем в линию
        magazinnaja = new Station("Магазинная", three);
        rynochnaja = new Station("Рыночная", three);
        bazarnaja = new Station("Базарная", three);
        vokzalnaja = new Station("Вокзальная", three);

        Stream.of(one, two, three).forEach(stationIndex::addLine);
        Stream.of(prohodnaja, podjemnaja, kabinetnaja, ofisnaja,
                        dorozhnaja, remontnaja, znakovaja, bordjurnaja,
                        magazinnaja, rynochnaja, bazarnaja, vokzalnaja)
                .peek(station -> station.getLine().addStation(station))
                .forEach(stationIndex::addStation);

        stationIndex.getConnectedStations(kabinetnaja);
        stationIndex.getConnectedStations(znakovaja);
        stationIndex.getConnectedStations(bordjurnaja);
        stationIndex.getConnectedStations(bazarnaja);

        stationIndex.addConnection(Stream.of(kabinetnaja, znakovaja).collect(Collectors.toList()));
        stationIndex.addConnection(Stream.of(bordjurnaja, bazarnaja).collect(Collectors.toList()));

        routeNoTransfer = Stream.of(dorozhnaja, remontnaja, znakovaja, bordjurnaja).collect(Collectors.toList());
        routeOneTransfer = Stream.of(dorozhnaja, remontnaja, znakovaja, bordjurnaja,
                bazarnaja, vokzalnaja).collect(Collectors.toList());
        routeTwoTransfer = Stream.of(ofisnaja, kabinetnaja, znakovaja, bordjurnaja,
                bazarnaja, vokzalnaja).collect(Collectors.toList());

    }

    //расчёт продолжительности
    public void testCalculateDuration() {
        double actual = RouteCalculator.calculateDuration(routeOneTransfer);
        double expected = 13.5;
        assertEquals(expected, actual);
    }
    //без пересадок
    public void testGetRouteOnTheLine() {
        List<Station> actual = routeCalculator.getShortestRoute(dorozhnaja, bordjurnaja);
        List<Station> expected = routeNoTransfer;
        assertEquals(expected, actual);
    }
    //с одной пересадкой
    public void testGetRouteWithOneConnection() {
        List<Station> actual = routeCalculator.getShortestRoute(dorozhnaja, vokzalnaja);
        List<Station> expected = routeOneTransfer;
        assertEquals(expected, actual);
    }
    //на линии
    public void testGetRouteViaConnectedLine() {
        //немного не понял, через что использовать данный метод
    }
    //с двумя пересадками
    public void testGetRouteWithTwoConnections() {
        List<Station> actual = routeCalculator.getShortestRoute(ofisnaja, vokzalnaja);
        List<Station> expected = routeTwoTransfer;
        assertEquals(expected, actual);
        System.out.println(actual);
        System.out.println(expected);
    }
}
