package com.appdev.shsappp;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * A fragment that launches other parts of the demo application.
 */
public class AnnouncementFragment extends Fragment {
	ArrayList<String> announcementsList = new ArrayList<String>();
	ArrayList<String> announcementsSender = new ArrayList<String>();
	ArrayList<String> announcementsTitle = new ArrayList<String>();
	ArrayList<String> createdAtTime = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_annoucements,
				container, false);

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
				"Loading...", true);
		Parse.initialize(getActivity(),
				"mBeDrmdeuRATh3rO7CqbTZMYKcXkuSrCKPEkPFDG",
				"VoIiZFddiKtfH9i7iz5jyQMsT9H45KgnDUOtEDo2");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Announcements");
		query.setLimit(1000);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> l, com.parse.ParseException e) {
				TextView connectToInternet = (TextView) getView().findViewById(
						R.id.connect_to_internet);
				if (e == null) {
					for (int i = 0; i < l.size(); i++) {
						announcementsList.add(l.get(i).getString("body"));
						announcementsSender.add(l.get(i).getString("sender"));
						announcementsTitle.add(l.get(i).getString("title"));
						String hours;
						if (l.get(i).getCreatedAt().getHours() < 12) {
							hours = l.get(i).getCreatedAt().getHours() + ":"
									+ l.get(i).getCreatedAt().getMinutes()
									+ "am";
							if(l.get(i).getCreatedAt().getMinutes()<10)
							hours = l.get(i).getCreatedAt().getHours() + ":0"
									+ l.get(i).getCreatedAt().getMinutes()
									+ "am";
						} else if (l.get(i).getCreatedAt().getHours() >= 13) {
							hours = (l.get(i).getCreatedAt().getHours() - 12)
									+ ":"
									+ l.get(i).getCreatedAt().getMinutes()
									+ "pm";
							if(l.get(i).getCreatedAt().getMinutes()<10)
								hours = (l.get(i).getCreatedAt().getHours() - 12)
								+ ":0"
								+ l.get(i).getCreatedAt().getMinutes()
								+ "pm";
						} else {
							hours = l.get(i).getCreatedAt().getHours() + ":"
									+ l.get(i).getCreatedAt().getMinutes()
									+ "pm";
							if(l.get(i).getCreatedAt().getMinutes()<10)
								hours = l.get(i).getCreatedAt().getHours() + ":0"
										+ l.get(i).getCreatedAt().getMinutes()
										+ "pm";
						}

						String time = (l.get(i).getCreatedAt().getMonth() + 1)
								+ "/" + (l.get(i).getCreatedAt().getDate() + 1)
								+ "/"
								+ (l.get(i).getCreatedAt().getYear() + 1900)
								+ " " + hours;
						// if time HH:MM is ever required
						// +", "+l.get(i).getCreatedAt().getHours()+":"+l.get(i).getCreatedAt().getMinutes();

						createdAtTime.add(time);

					}
					if (announcementsList.size() > 0) {
						dialog.dismiss();
						connectToInternet.setVisibility(View.GONE);
					}
					ListView announcementsListView = (ListView) getView()
							.findViewById(R.id.announcement_list);
					AnnounceListAdapter adapter = new AnnounceListAdapter(
							getActivity(), announcementsList);
					announcementsListView.setAdapter(adapter);

				} else {
					dialog.dismiss();
					connectToInternet
							.setText("Connect to the Internet to view announcements.");
				}
			}

		});

	}

	public class AnnounceListAdapter extends ArrayAdapter<String> {

		public AnnounceListAdapter(Context context, ArrayList<String> items) {
			super(context, R.layout.announcement_list_item_layout, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				v = LayoutInflater.from(getContext()).inflate(
						R.layout.announcement_list_item_layout, parent, false);
			}
			TextView title = (TextView) v.findViewById(R.id.title);
			TextView createdAt = (TextView) v.findViewById(R.id.createdAt);
			TextView sender = (TextView) v.findViewById(R.id.sender);
			TextView announcementText = (TextView) v
					.findViewById(R.id.body_text);
			title.setText(announcementsTitle.get(announcementsTitle.size() - 1
					- position));
			sender.setText(announcementsSender.get(announcementsSender.size()
					- 1 - position));
			createdAt.setText(createdAtTime.get(createdAtTime.size() - 1
					- position));

			announcementText.setText(announcementsList.get(announcementsList
					.size() - 1 - position));

			return v;

		}
	}
}
