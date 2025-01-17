/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.iosched.ui.sessioncommon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.samples.apps.iosched.databinding.ItemSessionBinding
import com.google.samples.apps.iosched.model.userdata.UserSession
import kotlinx.coroutines.flow.StateFlow
import org.threeten.bp.ZoneId

class SessionsAdapter(
    private val tagViewPool: RecycledViewPool,
    private val showReservations: StateFlow<Boolean>,
    private val timeZoneId: StateFlow<ZoneId>,
    private val lifecycleOwner: LifecycleOwner,
    private val onSessionClickListener: OnSessionClickListener,
    private val onSessionStarClickListener: OnSessionStarClickListener
) : ListAdapter<UserSession, SessionViewHolder>(SessionDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val binding = ItemSessionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ).apply {
            tags.apply {
                setRecycledViewPool(tagViewPool)
                // todo : FlexboxLayoutManager란 무엇인가?
                layoutManager = FlexboxLayoutManager(parent.context).apply {
                    recycleChildrenOnDetach = true
                }
            }
            showReservations = this@SessionsAdapter.showReservations
            timeZoneId = this@SessionsAdapter.timeZoneId
            showTime = false
            lifecycleOwner = this@SessionsAdapter.lifecycleOwner
            sessionClickListener = onSessionClickListener
            sessionStarClickListener = onSessionStarClickListener
        }
        return SessionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.binding.userSession = getItem(position)
        holder.binding.executePendingBindings()
    }
}

class SessionViewHolder(
    internal val binding: ItemSessionBinding
) : ViewHolder(binding.root)

object SessionDiff : DiffUtil.ItemCallback<UserSession>() {
    override fun areItemsTheSame(oldItem: UserSession, newItem: UserSession): Boolean {
        // We don't have to compare the userEvent id because it matches the session id.
        return oldItem.session.id == newItem.session.id
    }

    override fun areContentsTheSame(oldItem: UserSession, newItem: UserSession): Boolean {
        return oldItem == newItem
    }
}
