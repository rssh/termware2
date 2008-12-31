package ua.gradsoft.termwaredemos.life;


import java.awt.*;
import java.awt.event.*;

/**
 *
 * Subclass of Canvas which deals with the GameOfLife. <br>
 *
 * gui part is derived from org.bitstorm.gameoflife, by  Edwin Martin
 */
public class FieldCanvas extends Canvas {


	/**
	 * Constructor.
	 * @param Field
	 * @param cellSize size of cell in pixels
	 */
	public FieldCanvas(FieldModel fieldModel, int cellSize) {
		fieldModel_=fieldModel;
		cellSize_ = cellSize;
		fieldModel.clear();
                setDrawingEnabled(true);

		addMouseListener(
			new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
                                        //System.err.println("mousePressed");
                                        isMousePressed_=true;
				}
				public void mouseReleased(MouseEvent e) {
                                        //System.err.println("mouseReleased");
				        isMousePressed_=false;
                                        if (isDrawingEnabled_) {
                                          int mX=e.getX()/cellSize_;
                                          int mY=e.getY()/cellSize_;
                                          boolean oldState=fieldModel_.getCell(mX,mY);
                                          fieldModel_.setCell(mX,mY,!oldState);
					  repaint();
                                        }
				}

			});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
                            //System.err.println("mouseDragged");
                            if (isMousePressed_ && isDrawingEnabled_) {
                                int mX=e.getX()/cellSize_;
                                int mY=e.getY()/cellSize_;
                                //boolean oldState=fieldModel_.getCell(mX,mY);
                                fieldModel_.setCell(mX,mY,true);              
			        repaint();
                            }
			}
		});
	}

        /**
         * get new field model
         **/
        public FieldModel getFieldModel()
        {
            return fieldModel_;
        }
        
        /**
         * set new field model
         **/
        public void setFieldModel(FieldModel fieldModel)
        {
         fieldModel_=fieldModel;
         repaint(); 
        }



	/** 
	 * Use double buffering.
	 * @see java.awt.Component#update(java.awt.Graphics)
	 */
	public void update(Graphics theG) {
		Dimension d = getSize();
		if ((offScreenImage_ == null)) {
			offScreenImage_ = createImage(d.width, d.height);
			offScreenGraphics_ = offScreenImage_.getGraphics();
		}
		paint(offScreenGraphics_);
		theG.drawImage(offScreenImage_, 0, 0, null);
	}

	/**
	 * Draw this generation.
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		// draw background (MSIE doesn't do that)
		Dimension dim = new Dimension(fieldModel_.getNColumns(),fieldModel_.getNRows());
                g.setColor(Color.gray);
		g.fillRect(0, 0, cellSize_ * dim.width - 1, cellSize_ * dim.height - 1);
		// draw grid
		g.setColor(getBackground());
		for (int x = 1; x < dim.width; x++) {
			g.drawLine(x * cellSize_ - 1, 0, x * cellSize_ - 1, cellSize_ * dim.height - 1);
		}
		for (int y = 1; y < dim.height; y++) {
			g.drawLine( 0, y * cellSize_ - 1, cellSize_ * dim.width - 1, y * cellSize_ - 1);
		}
		// draw populated cells
		g.setColor(Color.yellow);
		for (int y = 0; y < dim.height; y++) {
			for (int x = 0; x < dim.width; x++) {
				if (fieldModel_.getCell(x, y)) {
					g.fillRect(x * cellSize_, y * cellSize_, cellSize_ - 1, cellSize_ - 1);
				}
			}
		}
	}
	
	/**
	 * This is the preferred size.
	 * @see java.awt.Component#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		return new Dimension( cellSize_ * fieldModel_.getNColumns(),cellSize_ * fieldModel_.getNRows() );
	}

        public  boolean isDrawingEnabled()
        { return isDrawingEnabled_; }
        
        public  void    setDrawingEnabled(boolean v)
        { isDrawingEnabled_=v; }
        
        public void clear()
        {
          fieldModel_.clear();
          repaint();
        }

        private boolean isMousePressed_;
        private boolean isDrawingEnabled_;
	private Image offScreenImage_ = null;
	private Graphics offScreenGraphics_;

	private int cellSize_;
	private FieldModel fieldModel_;


}
