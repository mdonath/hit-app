package nl.scouting.hit.app.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nl.scouting.hit.app.R;
import nl.scouting.hit.app.model.HitIcon;

/**
 * Shows all icons in a row.
 */
public class HitIconBarView extends HorizontalScrollView {

	public HitIconBarView(Context context) {
		super(context);
	}

	public HitIconBarView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	public HitIconBarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Adds all icons to the view.
	 *
	 * @param icons a list of HitIcon's
	 */
	public void init(final List<HitIcon> icons) {
		final Context ctx = getContext();

		LinearLayout iconsView = new LinearLayout(ctx);
		iconsView.setOrientation(LinearLayout.HORIZONTAL);
		for (final HitIcon icon : icons) {
			iconsView.addView(new HitIconView(ctx, icon));
		}
		addView(iconsView);
		setFadingEdgeLength(20);
	}

	private long tooltipLastStartedAt = 0;

	/** An ImageView using HitIcon as Data. */
	private class HitIconView extends ImageView {

		public HitIconView(final Context context, final HitIcon icon) {
			super(context);
			setImageResource(icon.getResId());

			final TextView tooltipView = (TextView) ((ViewGroup) HitIconBarView.this.getParent()).findViewById(R.id.tooltip);
			if (tooltipView != null) {
				setOnClickListener(new View.OnClickListener() {

					private static final long NORMAL_DELAY = 4000;
					private static final long LONG_DELAY = 6000;

					public void onClick(View v) {
						// show text in tooltip
						tooltipView.setText(icon.getTekst());
						tooltipView.setVisibility(View.VISIBLE);
						tooltipView.bringToFront();
						tooltipLastStartedAt = System.currentTimeMillis();
						// hide after a pause
						tooltipView.postDelayed(new Runnable() {
							@Override
							public void run() {
								// if *another* thread was started less than 2900 ms ago then do NOT hide
								if (System.currentTimeMillis() - tooltipLastStartedAt > NORMAL_DELAY - 100) {
									tooltipView.setVisibility(View.INVISIBLE);
								}
							}
						}, calculateDelay());
					}

					private long calculateDelay() {
						final long delayMillis;
						if (icon.getTekst().length() > 30) {
							delayMillis = LONG_DELAY;
						} else {
							delayMillis = NORMAL_DELAY;
						}
						return delayMillis;
					}
				});
			}
		}
	}
}
