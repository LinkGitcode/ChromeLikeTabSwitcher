/*
 * Copyright 2016 - 2017 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.android.tabswitcher.layout.tablet;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;

import de.mrapp.android.tabswitcher.R;
import de.mrapp.android.tabswitcher.TabSwitcher;
import de.mrapp.android.tabswitcher.layout.AbstractArithmetics;
import de.mrapp.android.tabswitcher.layout.AbstractDragEventHandler.DragState;
import de.mrapp.android.tabswitcher.model.AbstractItem;

import static de.mrapp.android.util.Condition.ensureNotNull;
import static de.mrapp.android.util.Condition.ensureTrue;

/**
 * Provides methods, which allow to calculate the position, size and rotation of a {@link
 * TabSwitcher}'s tabs, when using the tablet layout.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class TabletArithmetics extends AbstractArithmetics {

    /**
     * The height of a tab in pixels.
     */
    private final int tabHeight;

    /**
     * The height of the container, which contains tabs, in pixels.
     */
    private final int tabContainerHeight;

    /**
     * The offset between two neighboring tabs in pixels.
     */
    private final int tabOffset;

    /**
     * Creates a new class, which provides methods, which allow to calculate the position, size and
     * rotation of a {@link TabSwitcher}'s tabs, when using the tablet layout.
     *
     * @param tabSwitcher
     *         The tab switcher, the arithmetics should be calculated for, as an instance of the
     *         class {@link TabSwitcher}. The tab switcher may not be null
     */
    public TabletArithmetics(@NonNull final TabSwitcher tabSwitcher) {
        super(tabSwitcher);
        Resources resources = tabSwitcher.getResources();
        this.tabHeight = resources.getDimensionPixelSize(R.dimen.tablet_tab_height);
        this.tabContainerHeight =
                resources.getDimensionPixelSize(R.dimen.tablet_tab_container_height);
        this.tabOffset = resources.getDimensionPixelSize(R.dimen.tablet_tab_offset);
    }

    @Override
    public final int getTabSwitcherPadding(@NonNull final Axis axis, final int gravity) {
        ensureNotNull(axis, "The axis may not be null");
        ensureTrue(gravity == Gravity.START || gravity == Gravity.END, "Invalid gravity");
        if (axis == Axis.DRAGGING_AXIS) {
            return gravity == Gravity.START ? getTabSwitcher().getPaddingLeft() :
                    getTabSwitcher().getPaddingRight();
        } else {
            return gravity == Gravity.START ? getTabSwitcher().getPaddingTop() :
                    getTabSwitcher().getPaddingBottom();
        }
    }

    @Override
    public final float getTabContainerSize(@NonNull final Axis axis, final boolean includePadding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final float getTouchPosition(@NonNull final Axis axis,
                                        @NonNull final MotionEvent event) {
        ensureNotNull(axis, "The axis may not be null");
        ensureNotNull(event, "The motion event may not be null");

        if (axis == Axis.DRAGGING_AXIS) {
            return event.getX();
        } else {
            return event.getY();
        }
    }

    @Override
    public final float getTabPosition(@NonNull final Axis axis, @NonNull final AbstractItem item) {
        ensureNotNull(axis, "The axis may not be null");
        ensureNotNull(item, "The view may not be null");
        View view = item.getView();

        if (axis == Axis.DRAGGING_AXIS) {
            Toolbar[] toolbars = getTabSwitcher().getToolbars();
            return view.getX() - (getTabSwitcher().areToolbarsShown() && toolbars != null ?
                    Math.max(0,
                            toolbars[TabSwitcher.PRIMARY_TOOLBAR_INDEX].getWidth() - tabOffset) :
                    0);
        } else {
            return view.getY() - (tabContainerHeight - tabHeight);
        }
    }

    @Override
    public final void setTabPosition(@NonNull final Axis axis, @NonNull final AbstractItem item,
                                     final float position) {
        ensureNotNull(axis, "The axis may not be null");
        ensureNotNull(item, "The item may not be null");
        View view = item.getView();

        if (axis == Axis.DRAGGING_AXIS) {
            Toolbar[] toolbars = getTabSwitcher().getToolbars();
            view.setX((getTabSwitcher().areToolbarsShown() && toolbars != null ? Math.max(0,
                    toolbars[TabSwitcher.PRIMARY_TOOLBAR_INDEX].getWidth() - tabOffset) : 0) +
                    position);
        } else {
            view.setY((tabContainerHeight - tabHeight) + position);
        }
    }

    @Override
    public final void animateTabPosition(@NonNull final Axis axis,
                                         @NonNull final ViewPropertyAnimator animator,
                                         @NonNull final AbstractItem item, final float position,
                                         final boolean includePadding) {
        ensureNotNull(axis, "The axis may not be null");
        ensureNotNull(animator, "The animator may not be null");
        ensureNotNull(item, "The item may not be null");

        if (axis == Axis.DRAGGING_AXIS) {
            Toolbar[] toolbars = getTabSwitcher().getToolbars();
            animator.x((getTabSwitcher().areToolbarsShown() && toolbars != null ? Math.max(0,
                    toolbars[TabSwitcher.PRIMARY_TOOLBAR_INDEX].getWidth() - tabOffset) : 0) +
                    position);
        } else {
            animator.y((tabContainerHeight - tabHeight) + position);
        }
    }

    @Override
    public final float getTabScale(@NonNull final AbstractItem item, final boolean includePadding) {
        ensureNotNull(item, "The item may not be null");
        return 1;
    }

    @Override
    public final void setTabScale(@NonNull final Axis axis, @NonNull final AbstractItem item,
                                  final float scale) {
        ensureNotNull(axis, "The axis may not be null");
        ensureNotNull(item, "The item may not be null");
        View view = item.getView();

        if (axis == Axis.DRAGGING_AXIS) {
            view.setScaleX(scale);
        } else {
            view.setScaleY(scale);
        }
    }

    @Override
    public final void animateTabScale(@NonNull final Axis axis,
                                      @NonNull final ViewPropertyAnimator animator,
                                      final float scale) {
        ensureNotNull(axis, "The axis may not be null");
        ensureNotNull(animator, "The animator may not be null");

        if (axis == Axis.DRAGGING_AXIS) {
            animator.scaleX(scale);
        } else {
            animator.scaleY(scale);
        }
    }

    @Override
    public final float getTabSize(@NonNull final Axis axis, @NonNull final AbstractItem item) {
        ensureNotNull(axis, "The axis may not be null");
        ensureNotNull(item, "The item may not be null");
        View view = item.getView();

        if (axis == Axis.DRAGGING_AXIS) {
            return view.getWidth() * getTabScale(item);
        } else {
            return view.getHeight() * getTabScale(item);
        }
    }

    @Override
    public final float getTabPivot(@NonNull final Axis axis, @NonNull final AbstractItem item,
                                   @NonNull final DragState dragState) {
        ensureNotNull(axis, "The axis may not be null");
        ensureNotNull(item, "The item may not be null");
        ensureNotNull(dragState, "The drag state may not be null");
        return getTabSize(axis, item) / 2f;
    }

    @Override
    public void setTabPivot(@NonNull final Axis axis, @NonNull final AbstractItem item,
                            final float pivot) {
        ensureNotNull(axis, "The axis may not be null");
        ensureNotNull(item, "The item may not be null");
        View view = item.getView();

        if (axis == Axis.DRAGGING_AXIS) {
            view.setTranslationY(
                    view.getTranslationY() + (view.getPivotY() - pivot) * (1 - view.getScaleY()));
            view.setPivotY(pivot);
        } else {
            view.setTranslationX(
                    view.getTranslationX() + (view.getPivotX() - pivot) * (1 - view.getScaleX()));
            view.setPivotX(pivot);
        }
    }

    @Override
    public final float getTabRotation(@NonNull final Axis axis, @NonNull final AbstractItem item) {
        ensureNotNull(axis, "The axis may not be null");
        ensureNotNull(item, "The item may not be null");
        View view = item.getView();

        if (axis == Axis.DRAGGING_AXIS) {
            return view.getRotationX();
        } else {
            return view.getRotationY();
        }
    }

    @Override
    public final void setTabRotation(@NonNull final Axis axis, @NonNull final AbstractItem item,
                                     final float angle) {
        ensureNotNull(axis, "The axis may not be null");
        ensureNotNull(item, "The item may not be null");
        View view = item.getView();

        if (axis == Axis.DRAGGING_AXIS) {
            view.setRotationX(angle);
        } else {
            view.setRotationY(angle);
        }
    }

    @Override
    public final void animateTabRotation(@NonNull final Axis axis,
                                         @NonNull final ViewPropertyAnimator animator,
                                         final float angle) {
        ensureNotNull(axis, "The axis may not be null");
        ensureNotNull(animator, "The animator may not be null");

        if (axis == Axis.DRAGGING_AXIS) {
            animator.rotationX(angle);
        } else {
            animator.rotationY(angle);
        }
    }

}
