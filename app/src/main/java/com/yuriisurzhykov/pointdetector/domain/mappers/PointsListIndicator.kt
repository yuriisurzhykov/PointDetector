package com.yuriisurzhykov.pointdetector.domain.mappers

import com.google.firebase.database.GenericTypeIndicator
import com.yuriisurzhykov.pointdetector.domain.entities.Point

class PointsListIndicator : GenericTypeIndicator<java.util.ArrayList<Point>>()