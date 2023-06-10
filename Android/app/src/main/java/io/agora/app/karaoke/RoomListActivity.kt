package io.agora.app.karaoke

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.agora.app.karaoke.databinding.RoomListActivityBinding
import io.agora.app.karaoke.databinding.RoomListItemBinding
import io.agora.asceneskit.karaoke.KaraokeRoomActivity
import io.agora.asceneskit.karaoke.KaraokeUiKit
import io.agora.auikit.model.AUICommonConfig
import io.agora.auikit.model.AUICreateRoomInfo
import io.agora.auikit.model.AUIRoomInfo
import io.agora.auikit.ui.basic.AUIAlertDialog
import io.agora.auikit.ui.basic.AUISpaceItemDecoration
import io.agora.auikit.utils.BindingViewHolder
import java.util.Random

class RoomListActivity : AppCompatActivity() {

    private var viewBinding: RoomListActivityBinding? = null
    private val listAdapter by lazy { RoomListAdapter() }
    private var mList = listOf<AUIRoomInfo>()
    private val mUserId = Random().nextInt(99999999).toString()

    companion object {
        var ThemeId = io.agora.asceneskit.R.style.Theme_AKaraoke
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initService()
        fetchRoomList()
    }

    private fun initService() {
        // Create Common Config
        val config = AUICommonConfig()
        config.context = application
        config.userId = mUserId
        config.userName = "user_$mUserId"
        config.userAvatar = randomAvatar()
        // init AUIKit
        KaraokeUiKit.setup(
            config = config, // must
            BuildConfig.SERVER_HOST,
            ktvApi = null,// option
            rtcEngineEx = null, // option
            rtmClient = null // option
        )
    }

    private fun initView() {
        setTheme(ThemeId)
        val viewBinding = RoomListActivityBinding.inflate(LayoutInflater.from(this))
        this.viewBinding = viewBinding
        setContentView(viewBinding.root)

        val out = TypedValue()
        if (theme.resolveAttribute(android.R.attr.windowBackground, out, true)) {
            window.setBackgroundDrawableResource(out.resourceId)
        }

        viewBinding.rvList.addItemDecoration(
            AUISpaceItemDecoration(
                resources.getDimensionPixelSize(R.dimen.room_list_item_space_h),
                resources.getDimensionPixelSize(R.dimen.room_list_item_space_v)
            )
        )
        viewBinding.rvList.adapter = listAdapter

        viewBinding.swipeRefresh.setOnRefreshListener {
            refreshRoomList()
        }
        viewBinding.btnCreateRoom.setOnClickListener {
            AUIAlertDialog(this@RoomListActivity).apply {
                setTitle("房间主题")
                setInput("房间主题", randomRoomName(), true)
                setPositiveButton("一起嗨歌") {
                    dismiss()
                    createRoom(inputText)
                }
                show()
            }
        }
        viewBinding.btnSwitchTheme.setOnClickListener {
            if (ThemeId == io.agora.asceneskit.R.style.Theme_AKaraoke) {
                ThemeId = io.agora.asceneskit.R.style.Theme_AKaraoke_KTV
            } else {
                ThemeId = io.agora.asceneskit.R.style.Theme_AKaraoke
            }
            theme.setTo(resources.newTheme())
            initView()
        }
    }

    private fun createRoom(roomName: String) {
        val createRoomInfo = AUICreateRoomInfo()
        createRoomInfo.roomName = roomName
        KaraokeUiKit.createRoom(
            createRoomInfo,
            success = { roomInfo ->
                KaraokeRoomActivity.launch(this, roomInfo, ThemeId)
            },
            failure = {
                Toast.makeText(this@RoomListActivity, "Create room failed!", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }

    private fun refreshRoomList() {
        val viewBinding = this.viewBinding ?: return
        viewBinding.swipeRefresh.isRefreshing = true
        listAdapter.loadingMoreState = LoadingMoreState.Loading
        fetchRoomList()
    }

    private fun loadMore() {
        listAdapter.loadingMoreState = LoadingMoreState.Loading
        fetchRoomList()
    }

    private fun fetchRoomList() {
        val viewBinding = this.viewBinding ?: return
        var lastCreateTime: Long? = null
        if (!viewBinding.swipeRefresh.isRefreshing) {
            mList.lastOrNull()?.let {
                lastCreateTime = it.createTime
            }
        }
        KaraokeUiKit.getRoomList(lastCreateTime, 10,
            success = { roomList ->
                if (roomList.size < 10) {
                    listAdapter.loadingMoreState = LoadingMoreState.NoMoreData
                } else {
                    listAdapter.loadingMoreState = LoadingMoreState.Normal
                }
                mList = if (viewBinding.swipeRefresh.isRefreshing) { // 下拉刷新则重新设置数据
                    roomList
                } else {
                    val temp = mutableListOf<AUIRoomInfo>()
                    temp.addAll(mList)
                    temp.addAll(roomList)
                    temp
                }
                runOnUiThread {
                    viewBinding.swipeRefresh.isRefreshing = false
                    listAdapter.submitList(mList)
                }
            },
            failure = {
                runOnUiThread {
                    viewBinding.swipeRefresh.isRefreshing = false
                    listAdapter.loadingMoreState = LoadingMoreState.Normal
                    Toast.makeText(
                        this@RoomListActivity,
                        "Fetch room list failed!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }
    override fun onDestroy() {
        super.onDestroy()
        KaraokeUiKit.release()
    }

    private fun randomAvatar(): String {
        val randomValue = Random().nextInt(8) + 1
        return "https://accktvpic.oss-cn-beijing.aliyuncs.com/pic/sample_avatar/sample_avatar_${randomValue}.png"
    }

    private fun randomRoomName(): String {
        val randomValue = (Random().nextInt(9) + 1) * 10000 + Random().nextInt(10000)
        return "room_${randomValue}"
    }

    enum class LoadingMoreState {
        Normal,
        Loading,
        NoMoreData,
    }

    inner class RoomListAdapter :
        ListAdapter<AUIRoomInfo, BindingViewHolder<RoomListItemBinding>>(object :
            ItemCallback<AUIRoomInfo>() {

            override fun areItemsTheSame(oldItem: AUIRoomInfo, newItem: AUIRoomInfo) =
                oldItem.roomId == newItem.roomId

            override fun areContentsTheSame(
                oldItem: AUIRoomInfo,
                newItem: AUIRoomInfo
            ) = false
        }) {

        var loadingMoreState: LoadingMoreState = LoadingMoreState.NoMoreData

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ) =
            BindingViewHolder(RoomListItemBinding.inflate(LayoutInflater.from(parent.context)))

        override fun onBindViewHolder(
            holder: BindingViewHolder<RoomListItemBinding>,
            position: Int
        ) {
            val item = getItem(position)
            holder.binding.tvRoomName.text = item.roomName
            holder.binding.tvRoomOwner.text = item.roomOwner?.userName ?: "unKnowUser"
            holder.binding.tvMember.text = "${item.onlineUsers}人正在嗨歌"
            holder.binding.root.setOnClickListener { KaraokeRoomActivity.launch(this@RoomListActivity, item, ThemeId) }
            Glide.with(holder.binding.ivAvatar)
                .load(item.roomOwner?.userAvatar)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.binding.ivAvatar)

            if (loadingMoreState == LoadingMoreState.Normal && itemCount > 0 && position == itemCount - 1) {
                this@RoomListActivity.loadMore()
            }
        }
    }

}