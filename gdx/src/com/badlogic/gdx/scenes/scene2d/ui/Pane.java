/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.badlogic.gdx.scenes.scene2d.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.utils.ScissorStack;

public class Pane extends Container {	
	final PaneStyle style;
	final Stage stage;
	final Rectangle widgetBounds = new Rectangle();
	final Rectangle scissors = new Rectangle();
	
	public Pane(String name, Stage stage, int prefWidth, int prefHeight, PaneStyle style) {
		super(name, prefWidth, prefHeight);
		this.style = style;
		this.stage = stage;
		
		final NinePatch background = style.background;
		layout.padBottom = Integer.toString((int)(background.getBottomHeight()) + 1);
		layout.padTop = Integer.toString((int)(background.getTopHeight()) + 1);
		layout.padLeft = Integer.toString((int)(background.getLeftWidth()) + 1);
		layout.padRight = Integer.toString((int)(background.getRightWidth()) + 1);
	}	
	
	private void calculateScissors(Matrix4 transform) {
		final NinePatch background = style.background;
		
		widgetBounds.x = background.getLeftWidth();
		widgetBounds.y = background.getBottomHeight();
		widgetBounds.width = width - background.getLeftWidth() - background.getRightWidth();
		widgetBounds.height = height - background.getTopHeight() - background.getBottomHeight();
		ScissorStack.calculateScissors(stage.getCamera(), transform, widgetBounds, scissors);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		final NinePatch background = style.background;
		
		background.draw(batch, x, y, width, height);		
		setupTransform(batch);
		applyLayout();
		calculateScissors(batch.getTransformMatrix());
		ScissorStack.pushScissors(scissors);
		super.drawChildren(batch, parentAlpha);
		resetTransform(batch);
		ScissorStack.popScissors();		
	}
	
	public static class PaneStyle {
		public final NinePatch background;
		
		public PaneStyle(NinePatch backgroundPatch) {
			this.background = backgroundPatch;
		}
	}
}