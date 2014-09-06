package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.mdroid.activity.CourseContentActivity;
import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleCourse;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CourseFragment extends Fragment {
	/**
	 * List all courses in Moodle site
	 */
	public static final int TYPE_ALL_COURSES = 0;
	/**
	 * List only user courses
	 */
	public static final int TYPE_USER_COURSES = 1;
	/**
	 * List only courses favourited by user
	 */
	public static final int TYPE_FAV_COURSES = 2;

	CourseListAdapter courseListAdapter;
	SessionSetting session;
	List<MoodleCourse> mCourses;
	int Type = 0;

	/**
	 * All courses will be listed. You can choose something else by using
	 * Course(type) method.
	 */
	public CourseFragment() {
	}

	/**
	 * Course.TYPE_ALL_COURSES, TYPE_USER_COURSES, TYPE_FAV_COURSES are
	 * available options
	 * 
	 * @param Type
	 *            Choose what all courses have to listed.
	 */
	public CourseFragment(int Type) {
		this.Type = Type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_courses, container,
				false);

		// Get all courses of this site
		session = new SessionSetting(getActivity());
		mCourses = MoodleCourse.find(MoodleCourse.class, "siteid = ?",
				session.getCurrentSiteId() + "");

		ListView courseList = (ListView) rootView
				.findViewById(R.id.content_course);
		courseListAdapter = new CourseListAdapter(getActivity());
		courseList.setAdapter(courseListAdapter);

		return rootView;
	}

	public class CourseListAdapter extends ArrayAdapter<String> {
		private final Context context;

		public CourseListAdapter(Context context) {
			super(context, R.layout.list_item_account, new String[mCourses
					.size()]);
			this.context = context;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.list_item_course,
						parent, false);

				viewHolder.shortname = (TextView) convertView
						.findViewById(R.id.list_course_shortname);
				viewHolder.fullname = (TextView) convertView
						.findViewById(R.id.list_course_fullname);

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				// Just use the viewHolder and avoid findviewbyid()
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Assign values
			viewHolder.shortname.setText(mCourses.get(position).getShortname());
			viewHolder.fullname.setText(mCourses.get(position).getFullname());

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent i = new Intent(context, CourseContentActivity.class);
					i.putExtra("courseid", mCourses.get(position).getCourseid());
					i.putExtra("coursedbid", mCourses.get(position).getId());
					context.startActivity(i);
				}
			});

			return convertView;
		}
	}

	static class ViewHolder {
		TextView shortname;
		TextView fullname;
	}

}
