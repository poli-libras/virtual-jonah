package br.usp.libras.jonah;

/**
 * Possui funções para tentar criar interpolações mais interessantes que a interpolação linear 
 * fonte: www.robertpenner.com/easing/easing_demo.html
 * 
 * Não estamos usando nada disso
 *
 */
public class Easing {

	 ///////////// QUADRATIC EASING: t^2 ///////////////////

	// quadratic easing in - accelerating from zero velocity
	// t: current time, b: beginning value, c: change in value, d: duration
	// t and d can be in frames or seconds/milliseconds
	public static float easeInQuad(float t, float b, float c, float d) {
		return c*(t/=d)*t + b;
	}

	// quadratic easing out - decelerating to zero velocity
	public static float easeOutQuad(float t, float b, float c, float d) {
		return -c *(t/=d)*(t-2) + b;
	}

	// quadratic easing in/out - acceleration until halfway, then deceleration
	public static float easeInOutQuad(float t, float b, float c, float d) {
		if ((t/=d/2) < 1) return c/2*t*t + b;
		return -c/2 * ((--t)*(t-2) - 1) + b;
	}
	
	 ///////////// CUBIC EASING: t^3 ///////////////////////

	// cubic easing in - accelerating from zero velocity
	// t: current time, b: beginning value, c: change in value, d: duration
	// t and d can be frames or seconds/milliseconds
	public static float easeInCubic(float t, float b, float c, float d) {
		return c*(t/=d)*t*t + b;
	}

	// cubic easing out - decelerating to zero velocity
	public static float easeOutCubic(float t, float b, float c, float d) {
		return c*((t=t/d-1)*t*t + 1) + b;
	}

	// cubic easing in/out - acceleration until halfway, then deceleration
	public static float easeInOutCubic(float t, float b, float c, float d) {
		if ((t/=d/2) < 1) return c/2*t*t*t + b;
		return c/2*((t-=2)*t*t + 2) + b;
	}


	 ///////////// QUARTIC EASING: t^4 /////////////////////

	// quartic easing in - accelerating from zero velocity
	// t: current time, b: beginning value, c: change in value, d: duration
	// t and d can be frames or seconds/milliseconds
	public static float easeInQuart(float t, float b, float c, float d) {
		return c*(t/=d)*t*t*t + b;
	}

	// quartic easing out - decelerating to zero velocity
	public static float easeOutQuart(float t, float b, float c, float d) {
		return -c * ((t=t/d-1)*t*t*t - 1) + b;
	}

	// quartic easing in/out - acceleration until halfway, then deceleration
	public static float easeInOutQuart(float t, float b, float c, float d) {
		if ((t/=d/2) < 1) return c/2*t*t*t*t + b;
		return -c/2 * ((t-=2)*t*t*t - 2) + b;
	}


	 ///////////// QUINTIC EASING: t^5  ////////////////////

	// quintic easing in - accelerating from zero velocity
	// t: current time, b: beginning value, c: change in value, d: duration
	// t and d can be frames or seconds/milliseconds
	public static float easeInQuint(float t, float b, float c, float d) {
		return c*(t/=d)*t*t*t*t + b;
	}

	// quintic easing out - decelerating to zero velocity
	public static float easeOutQuint(float t, float b, float c, float d) {
		return c*((t=t/d-1)*t*t*t*t + 1) + b;
	}

	// quintic easing in/out - acceleration until halfway, then deceleration
	public static float easeInOutQuint(float t, float b, float c, float d) {
		if ((t/=d/2) < 1) return c/2*t*t*t*t*t + b;
		return c/2*((t-=2)*t*t*t*t + 2) + b;
	}

	 /////////// BOUNCE EASING: exponentially decaying parabolic bounce  //////////////

	// bounce easing in
	// t: current time, b: beginning value, c: change in position, d: duration
	public static float easeInBounce(float t, float b, float c, float d){
		return c - easeOutBounce (d-t, 0, c, d) + b;
	};

	// bounce easing out
	public static float easeOutBounce(float t, float b, float c, float d) {
		if ((t/=d) < (1/2.75)) {
			return c*(7.5625f*t*t) + b;
		} else if (t < (2/2.75)) {
			return c*(7.5625f*(t-=(1.5f/2.75))*t + .75f) + b;
		} else if (t < (2.5/2.75)) {
			return c*(7.5625f*(t-=(2.25f/2.75))*t + .9375f) + b;
		} else {
			return c*(7.5625f*(t-=(2.625f/2.75))*t + .984375f) + b;
		}
	};


}
