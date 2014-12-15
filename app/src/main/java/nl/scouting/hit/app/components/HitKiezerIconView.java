package nl.scouting.hit.app.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by martijn on 15-12-14.
 */
public class HitKiezerIconView extends ImageButton {

	public static enum IconState {
		SELECTED_YES, SELECTED_NO, UNSELECTED
	}

	private IconStateListener listener;

	public interface IconStateListener {
		void iconClicked(IconState state);
	}

	private IconState state = IconState.UNSELECTED;

	public HitKiezerIconView(Context context) {
		super(context);
		init();
	}

	public HitKiezerIconView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HitKiezerIconView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void setState(IconState state) {
		this.state = state;
		updateBackgroundToNewState();
	}

	private void init() {
		setBackgroundColor(Color.TRANSPARENT);
		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				nextState();
			}
		});
	}

	private void nextState() {
		switch (state) {
			case UNSELECTED:
				state = IconState.SELECTED_YES;
				break;
			case SELECTED_YES:
				state = IconState.SELECTED_NO;
				break;
			case SELECTED_NO:
				state = IconState.UNSELECTED;
				break;
		}
		if (listener != null) {
			listener.iconClicked(state);
		}

		updateBackgroundToNewState();
	}

	private void updateBackgroundToNewState() {
		switch (state) {
			case UNSELECTED:
				setBackgroundColor(Color.TRANSPARENT);
				break;
			case SELECTED_YES:
				setBackgroundColor(Color.GREEN);
				break;
			case SELECTED_NO:
				setBackgroundColor(Color.RED);
				break;
		}
	}

	public void setIconStateListener(IconStateListener listener) {
		this.listener = listener;
	}
}
