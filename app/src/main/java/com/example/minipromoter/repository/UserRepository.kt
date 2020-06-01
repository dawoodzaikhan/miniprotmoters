package com.example.minipromoter.repository

import androidx.lifecycle.LiveData
import com.example.minipromoter.App
import com.example.minipromoter.db.getDatabase
import com.example.minipromoter.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//
// Created by Abdul Basit on 3/8/2020.
//

class UserRepository {
    val database = getDatabase(App.getInstance())
/*


    fun getProductsList(): LiveData<List<ProductModel>> {
        return database.productDao.getAllProducts()
    }

    fun getProductsListWithOutLiveData(): List<ProductModel> {
        return database.productDao.getAllProductsWithOutLiveData()
    }

    fun getSingleUserWithOutLiveData(userID: String): UserModel {
        return database.userDao.getSingleUserWithOutLiveData(userID)
    }

    fun searchForProductPhoneNumber(phoneNumber: String): ProductModel {
        return database.productDao.searchForProduct(phoneNumber)
    }


    fun findUserByPhoneNumber(phoneNumber: String): UserModel {
        return database.userDao.findUser(phoneNumber)
    }

    */
/*  fun getProductUsersList(productId: Int): LiveData<List<UserModel>> {
          return database.userDao.getProductUser(productId)
      }*//*


    fun getAllCampaignMessages(campaignId: Long): LiveData<List<CampaignMessages>> {
        return database.campaignMessageDao.getAllCampaignMessages(campaignId)
    }

    */
/*fun getProductSubscribers(productId: Int): LiveData<List<UserModel>> {
        return database.userDao.getProductUser(productId)
    }*//*


    fun getProductCampaigns(productId: Long): LiveData<List<Campaign>> {
        return database.campaignDao.getProductCampaigns(productId)
    }

    fun getProductSubscribers(productId: Long): LiveData<List<UserModel>> {
        return database.productSubscribersDao.getProductSubscribers(productId)
    }


    suspend fun insertProduct(name: String) {
        withContext(Dispatchers.IO) {
            val productModel = ProductModel(productName = name)
            database.productDao.insertProduct(productModel)
        }
    }

    suspend fun insertCampaign(campaign: Campaign) {
        withContext(Dispatchers.IO) {
            database.campaignDao.insertCampaign(campaign)
        }
    }

    suspend fun insertCampaignMessage(campaignMessages: CampaignMessages) {
        withContext(Dispatchers.IO) {
            database.campaignMessageDao.insertCampaignMessage(campaignMessages)
        }
    }

    suspend fun insertProductSubscribers(productSubscriberes: ProductSubscribers) {
        withContext(Dispatchers.IO) {
            database.productSubscribersDao.insert(productSubscriberes)
        }
    }
*/
/*
    suspend fun insertUser(userModel: UserModel): Long {
        return withContext(Dispatchers.IO) {
            return@withContext database.userDao.insertUser(userModel)
        }
    }*/
}