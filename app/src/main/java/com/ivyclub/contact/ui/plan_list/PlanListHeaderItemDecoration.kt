package com.ivyclub.contact.ui.plan_list

import android.graphics.Canvas
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PlanListHeaderItemDecoration(private val sectionCallback: SectionCallback) :
    RecyclerView.ItemDecoration() {

    // onDrawOver 함수로 RecyclerView 위에 새로운 뷰를 그려준다.
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        Log.d("DRAW", "onDrawOver 호출")
        val topChild = parent.getChildAt(0) ?: return

        val topChildPosition  = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) return

        // 헤더뷰
        val currentHeader: View = sectionCallback.getHeaderView(parent, topChildPosition) ?: return

        // 헤더뷰 크기 지정
        fixHeaderSize(parent, currentHeader, topChild.measuredHeight)

        val contactPoint = currentHeader.bottom // 현재 헤더의 bottom이 부모의 top에서 얼마나 떨어졌는지
        val childInContact: View = getChildInContact(parent, contactPoint) ?: return

        val childAdapterPosition = parent.getChildAdapterPosition(childInContact)
        if (childAdapterPosition == RecyclerView.NO_POSITION) return

        when {
            sectionCallback.isHeader(childAdapterPosition) -> moveHeader(c, currentHeader, childInContact)
            else -> drawHeader(c, currentHeader)
        }

    }

    private fun moveHeader(c: Canvas, currentHeader: View, childInContact: View) {
        c.save()
        c.translate(0f, childInContact.top - currentHeader.height.toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    private fun drawHeader(c: Canvas, currentHeader: View) {
        c.save()
        c.translate(0f, 0f)
        currentHeader.draw(c)
        c.restore()
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child.bottom > contactPoint) {
                if (child.top <= contactPoint) {
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    private fun fixHeaderSize(parent: RecyclerView, currentHeader: View, measuredHeight: Int) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
            parent.width,
            View.MeasureSpec.EXACTLY
        )

        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            parent.height,
            View.MeasureSpec.EXACTLY
        )

        val childWidth: Int = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingStart + parent.paddingEnd,
            currentHeader.layoutParams.width
        )

        val childHeight: Int = ViewGroup.getChildMeasureSpec(
            heightSpec,
            0,
            measuredHeight
        )

        currentHeader.measure(childWidth, childHeight)
        currentHeader.layout(0, 0, currentHeader.measuredWidth, currentHeader.measuredHeight)
    }

    interface SectionCallback {
        // header가 고정되어있는지, 움직여야하는지 판단할 함수
        fun isHeader(position: Int): Boolean
        // 헤더 뷰를 반환하는 함수
        fun getHeaderView(list: RecyclerView, position: Int): View?
    }
}
