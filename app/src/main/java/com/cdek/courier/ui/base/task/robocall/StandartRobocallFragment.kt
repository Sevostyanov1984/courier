package com.cdek.courier.ui.base.task.robocall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cdek.courier.R
import com.cdek.courier.utils.enums.RobocallTypes
import kotlinx.android.synthetic.main.fragment_standart_robocall.*

class StandartRobocallFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_standart_robocall, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments?.getSerializable("type")
        when (type) {
            RobocallTypes.NORMAL -> message.text =
                "Сообщение: буду через час, доставка произойдет вовремя"
            RobocallTypes.MIN30 -> message.text = "Сообщение: опаздываю, буду через 30 минут"
            RobocallTypes.HOUR1 -> message.text = "Сообщение: опаздываю, буду через час"
            RobocallTypes.HOUR2 -> message.text = "Сообщение: опаздываю, буду через 2 часа"
            RobocallTypes.HOUR3 -> message.text = "Сообщение: опаздываю, буду через 3 часа"
            RobocallTypes.CHANGE -> {
                val hourStart = arguments?.getInt("hourStart")
                val minuteStart = arguments?.getInt("minuteStart")
                val hourEnd = arguments?.getInt("hourEnd")
                val minuteEnd = arguments?.getInt("minuteEnd")
                message.text = "Сообщение: подтвердите изменение времени приезда: " +
                        (if (hourStart == 0) "00" else hourStart) +
                        ":" + (if (minuteStart == 0) "00" else minuteStart) +
                        "-" + (if (hourEnd == 0) "00" else hourEnd) +
                        ":" + (if (minuteEnd == 0) "00" else minuteEnd)
            }
        }

        btn_cancel.setOnClickListener(View.OnClickListener {
            findNavController().navigateUp()
            //NavHostFragment.findNavController(this).navigate(R.id.taskItem)
        })
    }
}