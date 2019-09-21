package com.factorymarket.rxelm.sample.main.model

import com.factorymarket.rxelm.cmd.Cmd
import com.factorymarket.rxelm.components.paging.PagingState
import com.factorymarket.rxelm.contract.State
import com.factorymarket.rxelm.msg.Msg
import org.eclipse.egit.github.core.Repository

data class MainState(
    val isCanceled : Boolean = false,
    val userName: String,
    val reposList: PagingState<Repository, String>
) : State()


data class LoadReposCmd(val userName: String) : Cmd()

data class ReposLoadedMsg(val reposList: List<Repository>) : Msg()
object CancelMsg: Msg()
object RefreshMsg: Msg()
data class SubMsg(val time : Int): Msg()
data class Sub2Msg(val time : Int): Msg()