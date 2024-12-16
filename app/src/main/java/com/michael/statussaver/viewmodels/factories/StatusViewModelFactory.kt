package com.michael.statussaver.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.michael.statussaver.data.StatusRepository
import com.michael.statussaver.viewmodels.StatusViewModel

class StatusViewModelFactory(private val repository: StatusRepository) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return StatusViewModel(repository) as T
    }
}