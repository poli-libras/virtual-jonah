/*
 This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package br.usp.libras.jonah;

import processing.core.PApplet;
import processing.core.PVector;
import saito.objloader.OBJModel;

/**
 * This class inherits from OBJModel class (from OBJ Loader lib)
 *  to add the feature of transforming the model, by manipulating
 *  its vertices, in order to achieve the shape of another model 
 * 
 * Repository: http://code.google.com/p/objanim/
 * 
 * OBJ Loader project:
 * http://code.google.com/p/saitoobjloader/
 * 
 * @author Leonardo Leite, leonardofl87@gmail.com
 * http://code.google.com/u/leonardofl87/
 *
 */
public class AnimObj extends OBJModel {
	
	private float pass = 0.15f; // interpolation pass
	// higher pass is, faster will be the animation

	private PVector[] current, next;
	private float interp; // interp = 0: interpolation beginning; interp = 1: interpolation end
	
	/**
	 * Constructor
	 * 
	 * @param parent Processing sketch
	 * @param filepath OBJ model file path
	 */
	public AnimObj(PApplet parent, String filepath) {
	
		super(parent, filepath);
		this.interp = 1.0f; // starts stopped
		
		// copying vertices (caution to not copy by reference)
		this.current = new PVector[this.getVertexCount()];
		for(int i = 0; i < this.getVertexCount(); i++){			  
			PVector v = this.getVertex(i);
			this.current[i] = new PVector(v.x, v.y, v.z);
		}		  
	}

	/**
	 * Define how the current model must look like at the end of the next transformation
	 * It's very important that the vertices quantity and order are the same
	 * in the current and next models OBJ files
	 *  
	 * @param nextPose At the animation ends, this model must be shaped as the nextPose model
	 */
	public void setNextPose(OBJModel nextPose) {

                  // copying vertices (caution to not copy by reference)	    
		  this.next = new PVector[nextPose.getVertexCount()];
		  for(int i = 0; i < nextPose.getVertexCount(); i++){
			  PVector v = nextPose.getVertex(i);
			  this.next[i] = new PVector(v.x, v.y, v.z);
		  }		  
		  
		  this.interp = 1.0f; // starts stoped	  
	}
	
	/**
	 * Draws the interpolated model
	 */
	public void draw() {
	          // if necessary, refresh interpolation
		  if (interp < 1.0) {
		    
			interp = interp + this.pass; // interpolation increment
			    
			if (interp >= 1.0) { // interpolation end
			        interp = 1.0f;
			        this.current = this.next;
			}
  			this.interpola();
		  }

		  super.draw();
	}
	
	/**
	 * Unlock the transformation, that will take place as draw() is invoked
	 */
	public void startAnim() {
		
		this.interp = 0;
	}
	
	// performs the interpolation; function of this.interp
	private void interpola() {

	        for(int i = 0; i < this.current.length; i++){
		   PVector ult =  this.current[i]; 
		   PVector prox = this.next[i]; 
		   PVector tmpv = new PVector();
		   tmpv.x = PApplet.map(interp, 0, 1, ult.x, prox.x);
		   tmpv.y = PApplet.map(interp, 0, 1, ult.y, prox.y);
		   tmpv.z = PApplet.map(interp, 0, 1, ult.z, prox.z);
		   this.setVertex(i, tmpv);
		}		  
	}

	/**
	 * Define how faster is the interpolation (default is 0.15)
	 * @param pass
	 */
	public void setPass(float pass) {
	    
	    this.pass = pass;
	}

	public float getPass() {
	    
	    return this.pass;
	}

}