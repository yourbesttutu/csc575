package com.example.testrssi;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

public class MoveGestureDetector extends BaseGestureDetector
{

    private PointF mCurrentPointer;
    private PointF mPrePointer;
    //����Ϊ�˼��ٴ����ڴ�
    private PointF mDeltaPointer = new PointF();

    //���ڼ�¼���ս����������
    private PointF mExtenalPointer = new PointF();

    private OnMoveGestureListener mListenter;


    public MoveGestureDetector(Context context, OnMoveGestureListener listener)
    {
        super(context);
        mListenter = listener;
    }

    @Override
    protected void handleInProgressEvent(MotionEvent event)
    {
        int actionCode = event.getAction() & MotionEvent.ACTION_MASK;
        switch (actionCode)
        {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mListenter.onMoveEnd(this);
                resetState();
                break;
            case MotionEvent.ACTION_MOVE:
                updateStateByEvent(event);
                boolean update = mListenter.onMove(this);
                if (update)
                {
                    mPreMotionEvent.recycle();
                    mPreMotionEvent = MotionEvent.obtain(event);
                }
                break;

        }
    }

    @Override
    protected void handleStartProgressEvent(MotionEvent event)
    {
        int actionCode = event.getAction() & MotionEvent.ACTION_MASK;
        switch (actionCode)
        {
            case MotionEvent.ACTION_DOWN:
                resetState();//��ֹû�н��յ�CANCEL or UP ,�������
                mPreMotionEvent = MotionEvent.obtain(event);
                updateStateByEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mGestureInProgress = mListenter.onMoveBegin(this);
                break;
        }

    }

    protected void updateStateByEvent(MotionEvent event)
    {
        final MotionEvent prev = mPreMotionEvent;

        mPrePointer = caculateFocalPointer(prev);
        mCurrentPointer = caculateFocalPointer(event);

        //Log.e("TAG", mPrePointer.toString() + " ,  " + mCurrentPointer);

        boolean mSkipThisMoveEvent = prev.getPointerCount() != event.getPointerCount();

        //Log.e("TAG", "mSkipThisMoveEvent = " + mSkipThisMoveEvent);
        mExtenalPointer.x = mSkipThisMoveEvent ? 0 : mCurrentPointer.x - mPrePointer.x;
        mExtenalPointer.y = mSkipThisMoveEvent ? 0 : mCurrentPointer.y - mPrePointer.y;

    }

    /**
     * ����event�����ָ���ĵ�
     *
     * @param event
     * @return
     */
    private PointF caculateFocalPointer(MotionEvent event)
    {
        final int count = event.getPointerCount();
        float x = 0, y = 0;
        for (int i = 0; i < count; i++)
        {
            x += event.getX(i);
            y += event.getY(i);
        }

        x /= count;
        y /= count;

        return new PointF(x, y);
    }


    public float getMoveX()
    {
        return mExtenalPointer.x;

    }

    public float getMoveY()
    {
        return mExtenalPointer.y;
    }


    public interface OnMoveGestureListener
    {
        public boolean onMoveBegin(MoveGestureDetector detector);

        public boolean onMove(MoveGestureDetector detector);

        public void onMoveEnd(MoveGestureDetector detector);
    }

    public static class SimpleMoveGestureDetector implements OnMoveGestureListener
    {

        @Override
        public boolean onMoveBegin(MoveGestureDetector detector)
        {
            return true;
        }

        @Override
        public boolean onMove(MoveGestureDetector detector)
        {
            return false;
        }

        @Override
        public void onMoveEnd(MoveGestureDetector detector)
        {
        }
    }

}