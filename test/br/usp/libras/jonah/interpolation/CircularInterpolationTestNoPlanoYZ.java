package br.usp.libras.jonah.interpolation;

import static br.usp.libras.jonah.interpolation.Point.point;
import static org.junit.Assert.assertTrue;
import static processing.core.PConstants.PI;

import org.junit.Test;

import br.usp.libras.jonah.interpolation.CircularInterpolation;
import br.usp.libras.jonah.interpolation.Point;
import br.usp.libras.sign.transition.Path;

public class CircularInterpolationTestNoPlanoYZ {

    static final float TOLERANCIA = 1;
    
    // pontos do ponto de vista do interlocutor
    // (em YZ o que é horário pro emissor é anti-horário pro interlocutor)
    // Ox é direcionado da esquerda para direita
    // Oy é direcionado de cima para baixo
    // Oz é direcionado da dentro para fora da tela
    float c = 180; // cateto
    Point NORTE = point(0, -c, 0);
    Point SUL = point(0, c, 0);
    Point LESTE = point(0, 0, -c);
    Point OESTE = point(0, 0, c);
    float h = (float) (c * Math.sqrt(2)); // hipotenusa
    Point NORDESTE = point(0, -h, -h);
    Point NOROESTE = point(0, -h, h);
    Point SUDOESTE = point(0, h, h);
    Point SUDESTE = point(0, h, -h);
    
    CircularInterpolation interpolation;

    @Test
    public void should_interpolar_no_plano_yz_horario() {
        Point start = LESTE; 
        Point end = OESTE; 
        Path path = Path.CIRCULAR_HORARIO_EM_YZ;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, NORTE, end, SUL);
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
                " e o ponto obtido " + p + " foi de " + p.distanciaDe(expected),
                p.proximoDe(expected, TOLERANCIA));
//        System.out.println(p.distanciaDe(expected));
    }

    @Test
    public void should_interpolar_no_plano_yz_horario_com_x_desalinhado() {
        Point start = LESTE; 
        Point end = OESTE.comX(-30); 
        Path path = Path.CIRCULAR_HORARIO_EM_YZ;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, NORTE.comX(-15), end, SUL.comX(-15));
    }

    @Test
    public void should_interpolar_no_plano_yz_horario_invertendo_coordenadas_z() {
        Point start = OESTE; 
        Point end = LESTE; 
        Path path = Path.CIRCULAR_HORARIO_EM_YZ;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, SUL, end, NORTE);
    }
    
    @Test
    public void should_interpolar_no_plano_yz_horario_start_e_end_no_eixo_y() {
        Point start = NORTE; 
        Point end = SUL; 
        Path path = Path.CIRCULAR_HORARIO_EM_YZ;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, OESTE, end, LESTE);
    }
    
    @Test
    public void should_interpolar_no_plano_yz_horario_start_e_end_no_eixo_y_invertendo_y() {
        Point start = SUL; 
        Point end = NORTE; 
        Path path = Path.CIRCULAR_HORARIO_EM_YZ;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, LESTE, end, OESTE);
    }

    @Test
    public void should_interpolar_no_plano_yz_anti_horario() {
        Point start = LESTE; 
        Point end = OESTE; 
        Path path = Path.CIRCULAR_ANTI_HORARIO_EM_YZ;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, SUL, end, NORTE);
    }
    
    @Test
    public void should_interpolar_no_plano_yz_horario_start_no_quadrante1() {
        Point start = NORDESTE; 
        Point end = SUDOESTE; 
        Path path = Path.CIRCULAR_HORARIO_EM_YZ;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, NOROESTE, end, SUDESTE);
    }
    
    @Test
    public void should_interpolar_no_plano_yz_horario_start_no_quadrante2() {
        Point start = NOROESTE; 
        Point end = SUDESTE; 
        Path path = Path.CIRCULAR_HORARIO_EM_YZ;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, SUDOESTE, end, NORDESTE);
    }

    @Test
    public void should_interpolar_no_plano_yz_horario_start_no_quadrante3() {
        Point start = SUDOESTE; 
        Point end = NORDESTE; 
        Path path = Path.CIRCULAR_HORARIO_EM_YZ;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, SUDESTE, end, NOROESTE);
    }
    
    @Test
    public void should_interpolar_no_plano_yz_horario_start_no_quadrante4() {
        Point start = SUDESTE; 
        Point end = NOROESTE; 
        Path path = Path.CIRCULAR_HORARIO_EM_YZ;
        interpolation = new CircularInterpolation(start, end, path);
        assertCaminhoInterpoladoEh(start, NORDESTE, end, SUDOESTE);
    }

    @Test
    public void should_interpolar_no_plano_yz_horario_com_centro_fora_da_origem() {
        Point start = new Point(0, c, -c);
        Point end = new Point(0, c, c); 
        Path path = Path.CIRCULAR_HORARIO_EM_YZ;
        interpolation = new CircularInterpolation(start, end, path);
        
        Point zero = new Point(0,0,0);
        Point farEast = new Point(0, 2*c, 0);
        assertCaminhoInterpoladoEh(start, zero, end, farEast);
    }
}
