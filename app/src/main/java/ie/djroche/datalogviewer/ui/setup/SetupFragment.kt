package ie.djroche.datalogviewer.ui.setup

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.databinding.FragmentSetupBinding
import ie.djroche.datalogviewer.ui.setup.ui.theme.DataLogViewerTheme
import timber.log.Timber

// - https://stackoverflow.com/questions/59368360/how-to-use-compose-inside-fragment

class SetupFragment : Fragment() {
    private var _fragBinding: FragmentSetupBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var preferences: SharedPreferences
    // settings values
    private var useFirebase : Boolean = false
    private var apiEndpoint : String = "Not used"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentSetupBinding.inflate(inflater, container, false)
        val view = fragBinding.root
        setupMenu()
        val composeView: ComposeView = view.findViewById(R.id.compose_view)
        // Inflate the layout for this fragment
        composeView.apply {
            // Dispose the Composition when viewLifecycleOwner is destroyed
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                DataLogViewerTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        SetupUI()
                    }
                }
            }
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(view.context)
        useFirebase = preferences.getBoolean("FireBase",false)
        apiEndpoint = preferences.getString("API_Endpoint","EndPoint Not set").toString()
        return view
    }
//-----------------------------------------------------------------------------------------------
    private fun saveSettings()
    {
        val editor = preferences.edit()
        editor.putBoolean("FireBase",useFirebase)
        editor.putString("API_Endpoint",apiEndpoint)
        editor.apply()
        findNavController().popBackStack()
    }

    // ------------------------- RENDER the Menu for fragment--------------------------------------
    private fun setupMenu() {
        setHasOptionsMenu(true)
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_back, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return NavigationUI.onNavDestinationSelected(
                    menuItem, requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
/*-------------------------------------------------------------------------------------------
--------------------------------- Jetpack compose -------------------------------------------
-------------------------------------------------------------------------------------------*/
    @Composable
    fun SetupUI() {
        Column(modifier = Modifier.fillMaxSize())
        {
            // TopAppBar()
            ShowSettings()
        }
    }

//--------------------------------------------------------------------------------------------
    @Composable
    fun ShowSupportText(isError: Boolean) {
        if (isError)
            Text(
                text = "This Field is Required",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
            )
        else Text(text = "")
    }

//----------------------------------------------------------------------------------------------
    @OptIn(ExperimentalMaterial3Api::class)
    @Preview(showBackground = true)
    @Composable
    fun ShowSettings() {
        var xUseFirebase by remember { mutableStateOf(useFirebase) }
        var sApiEndpoint by remember { mutableStateOf(apiEndpoint) }
        var description by remember { mutableStateOf("") }
        var showError by remember { mutableStateOf(false) }
        DataLogViewerTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .border(border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.primary))
                        .fillMaxWidth()
                )
                    {
                        Text(text = " " + stringResource(id = R.string.db_type),
                            textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.width(width = 80.dp))
                        Switch(
                            checked = xUseFirebase,
                            onCheckedChange = {
                                xUseFirebase = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                                uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                            )
                        )// Switch
                        Spacer(modifier = Modifier.width(width = 8.dp))
                        Text(text = if (xUseFirebase)  stringResource(id = R.string.firebase) else stringResource(id = R.string.local))
                    }

                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        placeholderColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    value = sApiEndpoint,
                    onValueChange = {
                        sApiEndpoint = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(id = R.string.api_endpoint)) },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Edit, contentDescription = "add/edit",
                            tint = Color.Black
                        )
                    },
                )// outlined text field
                Button(
                    onClick = {
                        if (sApiEndpoint.isEmpty()) {
                            showError = true
                        } else {
                            useFirebase = xUseFirebase
                            apiEndpoint = sApiEndpoint
                            saveSettings()
                            Timber.i("jc Button pressed")
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    elevation = ButtonDefaults.buttonElevation(20.dp)
                ) {
                    Icon(Icons.Outlined.AddCircle, contentDescription = "Save")
                    Spacer(modifier = Modifier.width(width = 8.dp))
                    Text(stringResource(id = R.string.btn_save))
                }
            }
        }
    }


}