package br.usp.libras.sanbox;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import br.usp.libras.sign.transition.Path;

import static br.usp.libras.sanbox.Point.point;
import static processing.core.PConstants.*;

public class CircularInterpolationTest {

    static final float TOLERANCIA = 1;
    
    CircularInterpolation interpolation;

    @Test
    public void should_interpolar_no_plano_xy_horario() {
        Point start = point(180, 0, 0);
        Point end = point(-180, 0, 0);
        Path path = Path.CIRCULAR_HORARIO_EM_XY;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, point(0, 180, 0), end, point(0, -180, 0));
    }

    private void assertCaminhoInterpoladoEh(Point p0, Point ppi2, Point ppi, Point p3pi2) {

        Point p = interpolation.interpolate(0);
        assertPontosProximos(p0, p);

        p = interpolation.interpolate(PI/2);
        assertPontosProximos(ppi2, p);

        p = interpolation.interpolate(PI);
        assertPontosProximos(ppi, p);
        
        p = interpolation.interpolate(3*PI/2);
        assertPontosProximos(p3pi2, p);
    }
    
    private void assertPontosProximos(Point expected, Point p) {
        assertTrue("Distância entre o ponto esperado " + expected + 
                " e o ponto obtido " + p + " para foi de " + p.distanciaDe(expected),
                p.proximoDe(expected, TOLERANCIA));
//        System.out.println(p.distanciaDe(expected));
    }

    @Test
    public void should_interpolar_no_plano_xy_horario_com_z_desalinhado() {
        Point start = point(180, 0, 0);
        Point end = point(-180, 0, -30);
        Path path = Path.CIRCULAR_HORARIO_EM_XY;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, point(0, 180, -15), end, point(0, -180, -15));
    }
    
    @Test
    public void should_interpolar_no_plano_xy_horario_invertendo_coordenadas_x() {
        Point start = point(-180, 0, 0);
        Point end = point(180, 0, 0);
        Path path = Path.CIRCULAR_HORARIO_EM_XY;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, point(0, -180, 0), end, point(0, 180, 0));
    }
    
    @Ignore("Em andamento")
    @Test
    public void should_interpolar_no_plano_xy_horario_start_e_end_no_eixo_y() {
        Point start = point(0, -180, 0);
        Point end = point(0, 180, 0);
        Path path = Path.CIRCULAR_HORARIO_EM_XY;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, point(180, 0, 0), end, point(-180, 0, 0));
    }

    // TODO anti-horário em XY
    

}
