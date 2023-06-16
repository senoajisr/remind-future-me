package org.d3if3109.mobpro1.ui.showreminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.d3if3109.mobpro1.R
import org.d3if3109.mobpro1.adapter.ReminderAdapter
import org.d3if3109.mobpro1.data.SettingsDataStore
import org.d3if3109.mobpro1.data.dataStore
import org.d3if3109.mobpro1.databinding.FragmentShowReminderBinding
import org.d3if3109.mobpro1.db.ReminderDb
import org.d3if3109.mobpro1.db.ReminderEntity


class ShowReminderFragment : Fragment() {
    private lateinit var binding: FragmentShowReminderBinding
    private lateinit var reminderAdapter: ReminderAdapter
    private var isLinearLayout = true

    private val settingsDataStore: SettingsDataStore by lazy { SettingsDataStore(requireContext().dataStore) }

    private val viewModel: ShowReminderViewModel by lazy {
        val reminderDb = ReminderDb.getInstance(requireContext())
        val factory = ShowReminderViewModelFactory(reminderDb.dao)
        ViewModelProvider(this, factory)[ShowReminderViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentShowReminderBinding.inflate(layoutInflater, container, false)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_show_reminder, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menuItemShowReminderSwitchLayout -> {
                        lifecycleScope.launch {
                            settingsDataStore.saveLayout(!isLinearLayout, requireContext())
                        }

                        true
                    }
                    else -> false
                }
            }
        }, this.viewLifecycleOwner)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reminderAdapter = ReminderAdapter()

        with(binding.reminderRecyclerView) {
            adapter = reminderAdapter
        }

        settingsDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) {
            isLinearLayout = it
            setLayout()
            activity?.invalidateOptionsMenu()
        }

        viewModel.reminderData.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            reminderAdapter.submitList(it)
        }

        viewModel.status.observe(viewLifecycleOwner) {
            updateProgressBar(it)
        }

        binding.addReminderButton.setOnClickListener { onAddReminderButtonClicked(it) }

        addReminderItemSwipeRightAction()

        Glide.with(binding.welcomeImageView.context)
            .load("https://cdn.pixabay.com/photo/2015/05/28/09/08/hyacinth-787758_960_720.jpg")
            .error(R.drawable.baseline_broken_image_24)
            .into(binding.welcomeImageView)
    }

    private fun updateProgressBar(status: ShowReminderViewModel.ApiStatus) {
        when (status) {
            ShowReminderViewModel.ApiStatus.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            ShowReminderViewModel.ApiStatus.SUCCESS -> {
                binding.progressBar.visibility = View.GONE
            }
            ShowReminderViewModel.ApiStatus.FAILED -> {
                binding.progressBar.visibility = View.GONE
                binding.networkError.visibility = View.VISIBLE
            }
        }
    }

    private fun addReminderItemSwipeRightAction() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // From https://www.geeksforgeeks.org/android-swipe-to-delete-and-undo-in-recyclerview-with-kotlin/
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedReminder: ReminderEntity? = viewModel.reminderData.value?.get(position)

                if (deletedReminder != null) {
                    viewModel.removeReminderAt(deletedReminder.id)

                    Snackbar.make(
                        binding.reminderRecyclerView,
                        "Deleted " + deletedReminder.title,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(
                            "Undo"
                        ) {
                            viewModel.insertReminderEntity(deletedReminder)
                        }.show()
                }
            }
        }).attachToRecyclerView(binding.reminderRecyclerView)
    }

    private fun onAddReminderButtonClicked(it: View) {
        it.findNavController().navigate(R.id.action_showReminderFragment_to_addReminderFragment)
    }

    private fun setLayout() {
        binding.reminderRecyclerView.layoutManager =
            if (isLinearLayout) LinearLayoutManager(context)
            else GridLayoutManager(context, 2)
    }

    private fun setIcon(menuItem: MenuItem) {
        val iconId =
            if (isLinearLayout) R.drawable.baseline_grid_view_24
            else R.drawable.baseline_list_24

        menuItem.icon = ContextCompat.getDrawable(requireContext(), iconId)
    }
}