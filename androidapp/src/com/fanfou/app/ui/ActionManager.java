package com.fanfou.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;

import com.fanfou.app.App;
import com.fanfou.app.MessageChatPage;
import com.fanfou.app.MyProfilePage;
import com.fanfou.app.ProfilePage;
import com.fanfou.app.UserFavoritesPage;
import com.fanfou.app.UserListPage;
import com.fanfou.app.UserTimelinePage;
import com.fanfou.app.WritePage;
import com.fanfou.app.api.DirectMessage;
import com.fanfou.app.api.Status;
import com.fanfou.app.api.User;
import com.fanfou.app.config.Commons;
import com.fanfou.app.service.ActionService;
import com.fanfou.app.util.StatusHelper;
import com.fanfou.app.util.StringHelper;
import com.fanfou.app.util.Utils;

/**
 * @author mcxiaoke
 * @version 1.0 20110825
 * 
 */
public final class ActionManager {
	private static final String TAG = ActionManager.class.getSimpleName();

	private ActionManager() {
	}

	public static void doShowTimeline(Context context, final User user) {
		Intent intent = new Intent(context, UserTimelinePage.class);
		intent.putExtra(Commons.EXTRA_USER, user);
		context.startActivity(intent);
	}

	public static void doShowFavorites(Context context, final User user) {
		Intent intent = new Intent(context, UserFavoritesPage.class);
		intent.putExtra(Commons.EXTRA_USER, user);
		context.startActivity(intent);
	}

	public static void doShowFriends(Context context, final User user) {
		Intent intent = new Intent(context, UserListPage.class);
		intent.putExtra(Commons.EXTRA_USER, user);
		intent.putExtra(Commons.EXTRA_TYPE, User.TYPE_FRIENDS);
		context.startActivity(intent);
	}

	public static void doShowFollowers(Context context, final User user) {
		Intent intent = new Intent(context, UserListPage.class);
		intent.putExtra(Commons.EXTRA_USER, user);
		intent.putExtra(Commons.EXTRA_TYPE, User.TYPE_FOLLOWERS);
		context.startActivity(intent);
	}

	public static void doFollow(Context context, final User user,
			final ResultReceiver receiver) {
		int type = Commons.ACTION_USER_FOLLOW;
		if (user.following) {
			type = Commons.ACTION_USER_UNFOLLOW;
		}
		// ResultReceiver receiver = new MyResultReceiver();
		Intent intent = new Intent(context, ActionService.class);
		intent.putExtra(Commons.EXTRA_TYPE, type);
		intent.putExtra(Commons.EXTRA_ID, user.id);
		intent.putExtra(Commons.EXTRA_RECEIVER, receiver);
		context.startService(intent);

	}

	public static void doMyProfile(Context context) {
		Intent intent = new Intent(context, MyProfilePage.class);
		context.startActivity(intent);
		// if (App.me.user != null) {
		// Intent intent = new Intent(context, MyProfilePage.class);
		// intent.putExtra(Commons.EXTRA_USER, App.me.user);
		// context.startActivity(intent);
		// }else{
		// Intent intent = new Intent(context, MyProfilePage.class);
		// intent.putExtra(Commons.EXTRA_USER_ID, App.me.userId);
		// context.startActivity(intent);
		// }
	}

	public static void doProfile(Context context, String userId) {
		if (StringHelper.isEmpty(userId)) {
			if (App.DEBUG) {
				Log.e(TAG, "doProfile: userid is null.");
			}
			return;
		}
		if (userId.equals(App.me.userId)) {
			doMyProfile(context);
			return;
		}
		Intent intent = new Intent(context, ProfilePage.class);
		intent.putExtra(Commons.EXTRA_ID, userId);
		context.startActivity(intent);
	}

	public static void doProfile(Context context, DirectMessage dm) {
		if (dm == null || dm.isNull()) {
			if (App.DEBUG) {
				Log.e(TAG, "doProfile: status is null.");
			}
			return;
		}
		if (dm.senderId.equals(App.me.userId)) {
			doMyProfile(context);
			return;
		}
		Intent intent = new Intent(context, ProfilePage.class);
		intent.putExtra(Commons.EXTRA_ID, dm.senderId);
		intent.putExtra(Commons.EXTRA_USER_NAME, dm.senderScreenName);
		intent.putExtra(Commons.EXTRA_USER_HEAD, dm.senderProfileImageUrl);
		context.startActivity(intent);
	}

	public static void doProfile(Context context, Status status) {
		if (status == null || status.isNull()) {
			if (App.DEBUG) {
				Log.e(TAG, "doProfile: status is null.");
			}
			return;
		}
		if (status.userId.equals(App.me.userId)) {
			doMyProfile(context);
			return;
		}
		Intent intent = new Intent(context, ProfilePage.class);
		intent.putExtra(Commons.EXTRA_ID, status.userId);
		intent.putExtra(Commons.EXTRA_USER_NAME, status.userScreenName);
		intent.putExtra(Commons.EXTRA_USER_HEAD, status.userProfileImageUrl);
		context.startActivity(intent);
	}

	public static void doProfile(Context context, User user) {
		if (user == null || user.isNull()) {
			if (App.DEBUG) {
				Log.e(TAG, "doProfile: user is null.");
			}
			return;
		}
		if (user.id.equals(App.me.userId)) {
			doMyProfile(context);
			return;
		}
		Intent intent = new Intent(context, ProfilePage.class);
		intent.putExtra(Commons.EXTRA_USER, user);
		context.startActivity(intent);
	}

	public static void doShare(Context context, Status status) {
		if (status == null || status.isNull()) {
			if (App.DEBUG) {
				Log.e(TAG, "doShare: status is null.");
			}
			return;
		}
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "来自" + status.userScreenName
				+ "的饭否消息");
		intent.putExtra(Intent.EXTRA_TEXT,status.simpleText);
		context.startActivity(Intent.createChooser(intent, "分享"));
	}

	public static void doReply(Context context, Status status) {
		if (status == null || status.isNull()) {
			if (App.DEBUG) {
				Log.e(TAG, "doReply: status is null.");
			}
			return;
		}
		Intent intent = new Intent(context, WritePage.class);
		intent.putExtra(Commons.EXTRA_TYPE, WritePage.TYPE_REPLY);
		intent.putExtra(Commons.EXTRA_STATUS, status);
		context.startActivity(intent);
	}

	public static void doReply(Context context, final User user) {
		Intent intent = new Intent(context, WritePage.class);
		intent.putExtra(Commons.EXTRA_TYPE, WritePage.TYPE_NORMAL);
		intent.putExtra(Commons.EXTRA_TEXT, "@" + user.screenName + " ");
		context.startActivity(intent);
	}

	public static void doRetweet(Context context, Status status) {
		if (status == null || status.isNull()) {
			if (App.DEBUG) {
				Log.e(TAG, "doRetweet: status is null.");
			}
			return;
		}
		Intent intent = new Intent(context, WritePage.class);
		intent.putExtra(Commons.EXTRA_TYPE, WritePage.TYPE_REPOST);
		intent.putExtra(Commons.EXTRA_STATUS, status);
		context.startActivity(intent);
	}

	public static void doStatusDelete(final Activity activity, final String id) {
		doStatusDelete(activity, id, null);
	}

	public static void doStatusDelete(final Activity activity, final String id,
			final ResultListener li) {
		doStatusDelete(activity, id, li, false);
	}

	public static void doStatusDelete(final Activity activity, final String id,
			final boolean finish) {
		doStatusDelete(activity, id, null, finish);
	}

	public static void doStatusDelete(final Activity activity, final String id,
			final ResultListener li, final boolean finish) {
		if (StringHelper.isEmpty(id)) {
			if (App.DEBUG) {
				Log.e(TAG, "doStatusDelete: status id is null.");
			}
			return;
		}
		ResultReceiver receiver = new ResultReceiver(new Handler(
				activity.getMainLooper())) {

			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				switch (resultCode) {
				case Commons.RESULT_CODE_START:
					break;
				case Commons.RESULT_CODE_FINISH:
					Utils.notify(activity.getApplicationContext(), "删除成功");
					onSuccess(li, Commons.ACTION_STATUS_DELETE, "删除成功");
					if (finish && activity != null) {
						activity.finish();
					}
					break;
				case Commons.RESULT_CODE_ERROR:
					String msg = resultData
							.getString(Commons.EXTRA_ERROR_MESSAGE);
					Utils.notify(activity.getApplicationContext(), "删除失败："
							+ msg);
					onFailed(li, Commons.ACTION_STATUS_DELETE, "删除失败");
					break;
				default:
					break;
				}
			}
		};
		startService(activity, Commons.ACTION_STATUS_DELETE, id, receiver);
	}

	public static void doFavorite(final Activity activity, final Status status) {
		doFavorite(activity, status, null, false);
	}

	public static void doFavorite(final Activity activity, final Status status,
			boolean finish) {
		doFavorite(activity, status, null, finish);
	}

	public static void doFavorite(final Activity activity, final Status status,
			final ResultListener li) {
		doFavorite(activity, status, li, false);
	}

	public static void doFavorite(final Activity activity, final Status status,
			final ResultListener li, final boolean finish) {
		if (status == null || status.isNull()) {
			if (App.DEBUG) {
				Log.e(TAG, "doFavorite: status is null.");
			}
			return;
		}
		final int type = status.favorited ? Commons.ACTION_STATUS_UNFAVORITE
				: Commons.ACTION_STATUS_FAVORITE;
		ResultReceiver receiver = new ResultReceiver(new Handler(
				activity.getMainLooper())) {

			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				switch (resultCode) {
				case Commons.RESULT_CODE_START:
					break;
				case Commons.RESULT_CODE_FINISH:
					Status result = (Status) resultData
							.getSerializable(Commons.EXTRA_STATUS);
					String text = result.favorited ? "收藏成功" : "取消收藏成功";
					Utils.notify(activity.getApplicationContext(), text);
					onSuccess(li, type, text);
					if (finish) {
						activity.finish();
					}
					break;
				case Commons.RESULT_CODE_ERROR:
					String msg = resultData
							.getString(Commons.EXTRA_ERROR_MESSAGE);
					Utils.notify(activity.getApplicationContext(), "收藏失败："
							+ msg);
					onFailed(li, type, "收藏失败");
					break;
				default:
					break;
				}
			}
		};
		startService(activity, type, status.id, receiver);
	}
	
	public static void doMessageDelete(final Activity activity, final String id,
			final ResultListener li, final boolean finish) {
		if (StringHelper.isEmpty(id)) {
			if (App.DEBUG) {
				Log.d(TAG, "doMessageDelete: status id is null.");
			}
			return;
		}
		ResultReceiver receiver = new ResultReceiver(new Handler(
				activity.getMainLooper())) {

			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				switch (resultCode) {
				case Commons.RESULT_CODE_START:
					break;
				case Commons.RESULT_CODE_FINISH:
					Utils.notify(activity.getApplicationContext(), "删除成功");
					onSuccess(li, Commons.ACTION_DIRECT_MESSAGE_DELETE, "删除成功");
					if (finish && activity != null) {
						activity.finish();
					}
					break;
				case Commons.RESULT_CODE_ERROR:
					String msg = resultData
							.getString(Commons.EXTRA_ERROR_MESSAGE);
					Utils.notify(activity.getApplicationContext(), "删除失败："
							+ msg);
					onFailed(li, Commons.ACTION_DIRECT_MESSAGE_DELETE, "删除失败");
					break;
				default:
					break;
				}
			}
		};
		startService(activity, Commons.ACTION_DIRECT_MESSAGE_DELETE, id, receiver);
	}

	public static void doMessage(Context context, final User user) {
		final Intent intent = new Intent(context, MessageChatPage.class);
		intent.putExtra(Commons.EXTRA_USER_ID, user.id);
		intent.putExtra(Commons.EXTRA_USER_NAME, user.screenName);
		context.startActivity(intent);
	}

	private static void startService(Context context, int type, String id,
			ResultReceiver receiver) {
		Intent intent = new Intent(context, ActionService.class);
		intent.putExtra(Commons.EXTRA_TYPE, type);
		intent.putExtra(Commons.EXTRA_ID, id);
		intent.putExtra(Commons.EXTRA_RECEIVER, receiver);
		context.startService(intent);
	}

	private static void onSuccess(ResultListener li, int type, String message) {
		if (li != null) {
			li.onActionSuccess(type, message);
		}
	}

	private static void onFailed(ResultListener li, int type, String message) {
		if (li != null) {
			li.onActionFailed(type, message);
		}
	}

	public interface ResultListener {
		public void onActionSuccess(int type, String message);

		public void onActionFailed(int type, String message);
	}

	private static final int MSG_ACTION_SUCCESS = -1;
	private static final int MSG_ACTION_FAILED = -2;

	@SuppressWarnings("unused")
	private void sendMessage(int what, int type, String message) {
		// Message m = mHandler.obtainMessage();
		// m.what = what;
		// m.getData().putInt(Commons.EXTRA_TYPE, type);
		// m.getData().putString(Commons.EXTRA_TEXT, message);
		// mHandler.sendMessage(m);
	}

	@SuppressWarnings("unused")
	private static class ActionHandler extends Handler {
		private Activity a;
		private ResultListener l;

		public ActionHandler(Activity a, ResultListener l) {
			super(a.getMainLooper());
			this.a = a;
			this.l = l;
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ACTION_SUCCESS:
				if (l != null) {
					l.onActionSuccess(msg.getData().getInt(Commons.EXTRA_TYPE),
							msg.getData().getString(Commons.EXTRA_TEXT));
				}
				break;
			case MSG_ACTION_FAILED:
				if (l != null) {
					l.onActionFailed(msg.getData().getInt(Commons.EXTRA_TYPE),
							msg.getData().getString(Commons.EXTRA_TEXT));
				}
				break;
			default:
				break;
			}
		}

	}

}
