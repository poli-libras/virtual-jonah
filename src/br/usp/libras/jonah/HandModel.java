package br.usp.libras.jonah;

import br.usp.libras.sign.symbol.HandShape;
import br.usp.libras.sign.symbol.HandSide;

/**
 * Associação entre uma configuração de mão e seu lado (direito / esquerdo)
 * Serve para representar um modelo (obj) disponível
 * 
 * @author leonardo
 *
 */
public class HandModel {

	private HandShape config;
	private HandSide side;

	public HandModel() {
	}
	
	public HandModel(HandShape config, HandSide side) {
		this.config = config;
		this.side = side;
	}

	public HandShape getConfig() {
		return config;
	}
	public void setConfig(HandShape config) {
		this.config = config;
	}
	
	public HandSide getSide() {
		return side;
	}
	public void setSide(HandSide side) {
		this.side = side;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((config == null) ? 0 : config.hashCode());
		result = prime * result + ((side == null) ? 0 : side.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HandModel other = (HandModel) obj;
		if (config == null) {
			if (other.config != null)
				return false;
		} else if (!config.equals(other.config))
			return false;
		if (side == null) {
			if (other.side != null)
				return false;
		} else if (!side.equals(other.side))
			return false;
		return true;
	}
	
	
}
