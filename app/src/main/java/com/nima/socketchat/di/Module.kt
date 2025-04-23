package com.nima.socketchat.di

import android.content.Context
import androidx.compose.runtime.rememberCoroutineScope
import androidx.room.Room
import com.nima.socketchat.database.AppDatabase
import com.nima.socketchat.database.Converters
import com.nima.socketchat.repository.ChatRepository
import com.nima.socketchat.repository.MessageRepository
import com.nima.socketchat.repository.UserRepository
import com.nima.socketchat.screens.ClientUserChatScreen
import com.nima.socketchat.screens.LocalUserListScreen
import com.nima.socketchat.utils.ClientSocketManager
import com.nima.socketchat.utils.ServerSocketManager
import com.nima.socketchat.utils.UserIdentityManager
import com.nima.socketchat.utils.provideDataStore
import com.nima.socketchat.viewmodel.ClientUserChatViewModel
import com.nima.socketchat.viewmodel.ClientUsersListViewModel
import com.nima.socketchat.viewmodel.ConnectingViewModel
import com.nima.socketchat.viewmodel.HostSetupViewModel
import com.nima.socketchat.viewmodel.JoinServerViewModel
import com.nima.socketchat.viewmodel.LocalUserChatViewModel
import com.nima.socketchat.viewmodel.LocalUserListViewModel
import com.nima.socketchat.viewmodel.ServerUserChatViewModel
import com.nima.socketchat.viewmodel.ServerUsersListViewModel
import com.nima.socketchat.viewmodel.UserIdentityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.getScopeId
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import kotlin.coroutines.coroutineContext

val appModule = module {
    single { provideDataStore(androidContext()) }
    single { UserIdentityManager(get()) }

    single { Room.databaseBuilder(get(), AppDatabase::class.java, "socket_chat.db")
        .addTypeConverter(Converters())
        .fallbackToDestructiveMigration(false)
        .build() }

    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().chatDao() }
    single { get<AppDatabase>().messageDao() }

    single { UserRepository(get(), get()) }
    single { ChatRepository(get()) }
    single { MessageRepository(get()) }

    single { ClientSocketManager(get<MessageRepository>()) }
    single {
        ServerSocketManager(get<MessageRepository>()) }

    viewModel { UserIdentityViewModel(get()) }
    viewModel { ConnectingViewModel(get()) }
    viewModel { LocalUserListViewModel(get()) }
    viewModel { HostSetupViewModel(get<ServerSocketManager>(), get<UserIdentityManager>()) }
    viewModel { LocalUserChatViewModel(get<UserIdentityManager>(), get<MessageRepository>()) }
    viewModel { JoinServerViewModel(get<ClientSocketManager>(), get<UserIdentityManager>()) }
    viewModel { ClientUsersListViewModel(get<ClientSocketManager>(), get<UserIdentityManager>(), get<MessageRepository>()) }
    viewModel { ServerUsersListViewModel(get<ServerSocketManager>(), get<UserIdentityManager>(), get<MessageRepository>()) }
    viewModel { ClientUserChatViewModel(get<ClientSocketManager>(), get<MessageRepository>(), get<UserIdentityManager>()) }
    viewModel { ServerUserChatViewModel(get<ServerSocketManager>(), get<MessageRepository>(), get<UserIdentityManager>()) }
}