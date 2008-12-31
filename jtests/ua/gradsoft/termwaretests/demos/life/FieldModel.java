package ua.gradsoft.termwaretests.demos.life;

/**
 * Contains Field for Game Of Life.
 *
 */
class FieldModel {


	/**
	 * Contructor.
	 * 
	 * @param nRows number of rows
	 * @param nCols number of columns
	 */
	public FieldModel(int nRows, int nColumns) {
		nRows_ = nRows;
		nColumns_ = nColumns;
		cells_ = new boolean[nColumns][nRows];
                nCells_ = 0;
	}


	/**
	 * Clears grid.
	 */
	public void clear() {
		for (int x = 0; x < nColumns_; x++) {
			for (int y = 0; y < nRows_; y++) {
				cells_[x][y] = false;
			}
		}
                nCells_=0;
	}


	
	/**
	 * Get value of cell.
	 * @param x x-coordinate of cell
	 * @param y y-coordinate of cell
	 * @return value of cell
	 */
	public boolean getCell( int x, int y) {
	    return cells_[normalizeX(x)][normalizeY(y)];
	}
        

	/**
	 * Set value of cell.
	 * @param x x-coordinate of cell
	 * @param y y-coordinate of cell
	 * @param c value of cell
	 */
	public void setCell( int x, int y, boolean c ) {
                    int normalizedX=normalizeX(x);
                    int normalizedY=normalizeY(y);
                    boolean oldValue=cells_[normalizedX][normalizedY];
	            cells_[normalizedX][normalizedY] = c;
                    if (oldValue != c) {
                        if (c==true) {
                            ++nCells_;
                        }else{
                            --nCells_;
                        }
                    }
	}
	

	
        public int nNeighbourds(int x, int y)
        {
            return (
                   getCellInt(x-1,y-1)+getCellInt(x-1, y)+getCellInt(x-1, y+1)+
                   getCellInt(x  ,y-1)+                   getCellInt(x  , y+1)+
                   getCellInt(x+1,y-1)+getCellInt(x+1, y)+getCellInt(x+1, y+1)
                   );
        }
	
        public int getNRows()
        { return nRows_; }
        
        public int getNColumns()
        { return nColumns_; }

        int getCellInt(int x, int y)
        {
            return (getCell(x,y) ? 1 : 0);
        }

        private int normalizeX(int x)
        {
         x%=nColumns_;
         if (x<0) x+=nColumns_;
         return x;
        }
        
        private int normalizeY(int y)
        {
         y%=nRows_;
         if (y<0) y+=nRows_;
         return y;
        }

        
	private boolean cells_[][];
        private int nRows_;
        private int nColumns_;
        private int nCells_;


}
